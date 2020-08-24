/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	private PostProcessorRegistrationDelegate() {
	}


    /**
     * 调用两种后置处理器`BeanFactoryPostProcessor`,`BeanDefinitionRegistryPostProcessor`,这两个后置处理器是继续关系
     * 先执行所有子接口`BeanDefinitionRegistryPostProcessor`实例的方法，再调所有父接口`BeanFactoryPostProcessor`实例的方法
     * 详细执行顺序
     * 1. 执行`BeanDefinitionRegistryPostProcessor`的方法
     *    1.1. 手动添加的`BeanDefinitionRegistryPostProcessor`,通过{@link AbstractApplicationContext#addBeanFactoryPostProcessor}方法添加到{@link AbstractApplicationContext#beanFactoryPostProcessors}的bean
     *    1.2. beanFactory中实现了`PriorityOrdered`接口的`BeanDefinitionRegistryPostProcessor`。如`ConfigurationClassPostProcessor`
     *    1.3. beanFactory中实现了`Ordered`接口的`BeanDefinitionRegistryPostProcessor`
     *    1.4. 循环查找beanFactory中其它不需要排序的未执行的`BeanDefinitionRegistryPostProcessor`并执行，包含三种未以下三种：
     *         * `PriorityOrdered`，
     *         * `Ordered`的
     *         * 及在`BeanDefinitionRegistryPostProcessor`方法回调时动态注册到beanFactory的其它`BeanDefinitionRegistryPostProcessor`
     * 2. 执行`BeanFactoryPostProcessor`的方法
     *    2.1 执行所有`BeanDefinitionRegistryPostProcessor`类型的`BeanFactoryPostProcessor`方法
     *        前面的`BeanDefinitionRegistryPostProcessor`在执行回调前都保存在一个`List<BeanFactoryPostProcessor>`集合中
     *        循环这个集合，回调执行`BeanFactoryPostProcessor`的方法,所以这里`BeanFactoryPostProcessor`执行顺序跟前面的`BeanDefinitionRegistryPostProcessor`一样
     *    2.2 手动添加的`BeanFactoryPostProcessor`,通过{@link AbstractApplicationContext#addBeanFactoryPostProcessor}方法添加到{@link AbstractApplicationContext#beanFactoryPostProcessors}的bean
     *    2.3 beanFactory中实现了`PriorityOrdered`接口的`BeanFactoryPostProcessor`
     *    2.4 beanFactory中实现了`Ordered`接口的`BeanFactoryPostProcessor`
     *    2.5 beanFactory中其它不需要排序的`BeanFactoryPostProcessor`
     *
     * @param beanFactory
     * @param beanFactoryPostProcessors 手动调用AbstractApplicationContext#addBeanFactoryPostProcessor添加的后置处理器
     */
	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
        // 保存已经被调用过的`BeanDefinitionRegistryPostProcessor`的beanName，用于标记，避免重复调用
		Set<String> processedBeans = new HashSet<>();

		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			// 保存手动添加的BeanFactoryPostProcessor
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			// BeanDefinitionRegistryPostProcessor继承BeanFactoryPostProcessor,保存起来后续可以直接遍历集合回调BeanFactoryPostProcessor的方法
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
                    //1.1. 执行手动添加的`BeanDefinitionRegistryPostProcessor`
                    //     通过` AbstractApplicationContext#addBeanFactoryPostProcessor`方法添加到`AbstractApplicationContext#beanFactoryPostProcessors`的bean
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					//添加到集合中，后续回调BeanFactoryPostProcessor的方法
					registryProcessors.add(registryProcessor);
				}
				else {
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
            // 保存指定类型的BeanDefinitionRegistryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
            // start --- 1.2. 回调beanFactory中实现了`PriorityOrdered`接口的`BeanDefinitionRegistryPostProcessor`
            String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
			    // 从beanFactory中查找实现了PriorityOrdered接口的BeanDefinitionRegistryPostProcessor
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			// 排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			// 将实现了PriorityOrdered接口的BeanDefinitionRegistryPostProcessor合并到集合中，后续回调BeanFactoryPostProcessor的方法
			registryProcessors.addAll(currentRegistryProcessors);
            // BeanDefinitionRegistryPostProcessor方法回调
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			// 清信息集合,以便后面再次使用
			currentRegistryProcessors.clear();
			//  end ---  1.2. 回调beanFactory中实现了`PriorityOrdered`接口的`BeanDefinitionRegistryPostProcessor`


			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
            // start --- 1.3. 回调beanFactory中实现了`Ordered`接口的`BeanDefinitionRegistryPostProcessor`
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
                // 从beanFactory中查找实现了Ordered接口且未处理回调的BeanDefinitionRegistryPostProcessor
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
            // 排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			// 将实现了Ordered接口的BeanDefinitionRegistryPostProcessor合并到集合中，后续回调BeanFactoryPostProcessor的方法
			registryProcessors.addAll(currentRegistryProcessors);
			// BeanDefinitionRegistryPostProcessor方法回调
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
            // 清信息集合,以便后面再次使用
			currentRegistryProcessors.clear();
            // end --- 1.3. 回调beanFactory中实现了`Ordered`接口的`BeanDefinitionRegistryPostProcessor`


			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
            // start --- 1.4. 循环查找beanFactory中其它不需要排序的未执行的`BeanDefinitionRegistryPostProcessor`并执行
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
				    // 查找其它未处理回调的BeanDefinitionRegistryPostProcessor
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						// BeanDefinitionRegistryPostProcessor方法可能会beanFactory添加其它BeanDefinitionRegistryPostProcessor，因此需要再次循环
						reiterate = true;
					}
				}
				// 排序
				sortPostProcessors(currentRegistryProcessors, beanFactory);
                // 合并到集合中，后续回调BeanFactoryPostProcessor的方法
				registryProcessors.addAll(currentRegistryProcessors);
				// BeanDefinitionRegistryPostProcessor方法回调
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}
            // end --- 1.4. 循环查找beanFactory中其它不需要排序的未执行的`BeanDefinitionRegistryPostProcessor`并执行

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
            // 2.1 回调所有`BeanDefinitionRegistryPostProcessor`类型的`BeanFactoryPostProcessor`方法
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			// 2.2 回调手动添加的`BeanFactoryPostProcessor`的方法
            // 通过AbstractApplicationContext#addBeanFactoryPostProcessor方法添加到AbstractApplicationContext#beanFactoryPostProcessors的bean
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
        // 获取beanFactory中所有BeanFactoryPostProcessor的beanName
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
        // 所有实现了PriorityOrdered接口BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		// 所有实现了Ordered接口BeanFactoryPostProcessor的beanName
		List<String> orderedPostProcessorNames = new ArrayList<>();
		// 其它不需要排序的BeanFactoryPostProcessor的beanName
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		// 2.3 beanFactory中实现了`PriorityOrdered`接口的`BeanFactoryPostProcessor`,
        // 排序，回调
        sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
        // 2.4 beanFactory中实现了`Ordered`接口的`BeanFactoryPostProcessor`
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 排序，回调
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);


		// Finally, invoke all other BeanFactoryPostProcessors.
        // 2.5 beanFactory中其它的`BeanFactoryPostProcessor`
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 不需要排序，直接回调
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		beanFactory.clearMetadataCache();
	}

    /**
     * 调用两种后置处理器`BeanFactoryPostProcessor`,`BeanDefinitionRegistryPostProcessor`,这两个后置处理器是继续关系
     * 先执行所有子接口`BeanDefinitionRegistryPostProcessor`实例的方法，再调所有父接口`BeanFactoryPostProcessor`实例的方法
     * 详细执行顺序
     * 1. 执行`BeanDefinitionRegistryPostProcessor`的方法
     *    1.1. 手动添加的`BeanDefinitionRegistryPostProcessor`,通过{@link AbstractApplicationContext#addBeanFactoryPostProcessor}方法添加到{@link AbstractApplicationContext#beanFactoryPostProcessors}的bean
     *    1.2. beanFactory中实现了`PriorityOrdered`接口的`BeanDefinitionRegistryPostProcessor`。如`ConfigurationClassPostProcessor`
     *    1.3. beanFactory中实现了`Ordered`接口的`BeanDefinitionRegistryPostProcessor`
     *    1.4. 循环查找beanFactory中其它不需要排序的未执行的`BeanDefinitionRegistryPostProcessor`并执行，包含三种未以下三种：
     *         * `PriorityOrdered`，
     *         * `Ordered`的
     *         * 及在`BeanDefinitionRegistryPostProcessor`方法回调时动态注册到beanFactory的其它`BeanDefinitionRegistryPostProcessor`
     * 2. 执行`BeanFactoryPostProcessor`的方法
     *    2.1 执行所有`BeanDefinitionRegistryPostProcessor`类型的`BeanFactoryPostProcessor`方法
     *        前面的`BeanDefinitionRegistryPostProcessor`在执行回调前都保存在一个`List<BeanFactoryPostProcessor>`集合中
     *        循环这个集合，回调执行`BeanFactoryPostProcessor`的方法,所以这里`BeanFactoryPostProcessor`执行顺序跟前面的`BeanDefinitionRegistryPostProcessor`一样
     *    2.2 手动添加的`BeanFactoryPostProcessor`,通过{@link AbstractApplicationContext#addBeanFactoryPostProcessor}方法添加到{@link AbstractApplicationContext#beanFactoryPostProcessors}的bean
     *    2.3 beanFactory中实现了`PriorityOrdered`接口的`BeanFactoryPostProcessor`
     *    2.4 beanFactory中实现了`Ordered`接口的`BeanFactoryPostProcessor`
     *    2.5 beanFactory中其它不需要排序的`BeanFactoryPostProcessor`
     *
     * @param beanFactory
     * @param beanFactoryPostProcessors 手动调用AbstractApplicationContext#addBeanFactoryPostProcessor添加的后置处理器
     */
    /**
     * 实例化所有的`BeanPostProcessor`处理器，并向工厂注册(保存到一个专门保存BeanPostProcessor实例的集合中)<pre/>
     * 详细的注册顺序：
     * 1.手动绑定BeanPostProcessorChecker处理器
     * 2.找出所有实现了PriorityOrdered接口的处理器，排序并注册
     * 3.找出所有实现了Ordered接口的处理器，排序并注册
     * 4.找出其它处理器进行注册
     * 5.找出其它实现了MergedBeanDefinitionPostProcessor接口的处理器。即便实现了PriorityOrdered或Ordered接口的处理器，在时会被重新注册
     * 6.手动绑定ApplicationListenerDetector处理器
     * @param beanFactory
     * @param applicationContext
     */
	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		// tips: 绑定BeanPostProcessorChecker处理器
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, register the BeanPostProcessors that implement PriorityOrdered.
        // tips: 1. 找出所有实现了PriorityOrdered接口的处理器，排序并注册
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// Next, register the BeanPostProcessors that implement Ordered.
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
        // tips: 2. 找出所有实现了Ordered接口的处理器，排序并注册
		sortPostProcessors(orderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}

		// tips: 3. 找出其它处理器进行注册
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// Finally, re-register all internal BeanPostProcessors.
        // tips: 4. 最后，找出其它实现了MergedBeanDefinitionPostProcessor接口的处理器
        //  即便实现了PriorityOrdered或Ordered接口的处理器，在时会被重新注册,因为注册是先 remove(beanPostProcessor)再 add(beanPostProcessor)
		sortPostProcessors(internalPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
        // tips: 手动绑定ApplicationListenerDetector处理器
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
