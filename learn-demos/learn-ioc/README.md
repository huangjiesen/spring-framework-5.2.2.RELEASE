### What is IoC
控制反转（`Inversion of Control`，缩写为`IoC`），是面向对象编程中的一种设计原则，可以用来减低计算机代码之间的耦合度。其中最常见的实现方式叫做依赖注入（`Dependency Injection`，简称`DI`）,换言之`DI`是`IoC`的一种实现方式。还有另外两种方式叫依赖查找（`Dependency Lookup`）及依赖拖拽(`Dependency Pull`)


### spring实现IOC的思路和方法
spring实现IOC的思路是提供一些配置信息用来描述类之间的依赖关系，然后由容器去解析这些配置信息，继而维护好对象之间的依赖关系，前提是对象之间的依赖关系必须在类中定义好，比如A.class中有一个B.class的属性，那么我们可以理解为A依赖了B。既然我们在类中已经定义了他们之间的依赖关系那么为什么还需要在配置文件中去描述和定义呢？

spring实现IOC的思路大致可以拆分成3点
1. 应用程序中提供类，提供依赖关系（属性或者构造方法）
1. 把需要交给容器管理的对象通过配置信息告诉容器（xml、annotation，javaconfig）
1. 把各个类之间的依赖关系通过配置信息告诉容器

<br/>

1. 配置这些信息的方法有三种分别是xml，annotation和javaconfig
1. 维护的过程称为自动注入，自动注入的方法有两种构造方法和setter
1. 自动注入的值可以是对象，数组，map，list和常量比如字符串整形等


# AnnotationConfigApplicationContext启动过程
1. new AnnotationConfigApplicationContext(Application.class);   
    * 初始化时父类或自己创建的一些对象
       * this.classLoader = ClassUtils.getDefaultClassLoader();         // org.springframework.core.io.DefaultResourceLoader.DefaultResourceLoader()
       * this.resourcePatternResolver = getResourcePatternResolver();   // torg.springframework.context.support.AbstractApplicationContext.AbstractApplicationContext()
       * this.beanFactory = new DefaultListableBeanFactory();           // org.springframework.context.support.GenericApplicationContext.GenericApplicationContext()
       * this.reader = new AnnotatedBeanDefinitionReader(this);
       * this.scanner = new ClassPathBeanDefinitionScanner(this);
       
    
    
    
# AnnotationConfigApplicationContext初始化重点调用链
```   
org.springframework.context.annotation.AnnotationConfigApplicationContext.AnnotationConfigApplicationContext(java.lang.Class<?>...)
    // this(); 无参方法调用
    1. org.springframework.context.annotation.AnnotationConfigApplicationContext.AnnotationConfigApplicationContext()
        // this.classLoader = ClassUtils.getDefaultClassLoader(); // 初始classLoader （父类构造方法）
        org.springframework.core.io.DefaultResourceLoader.DefaultResourceLoader()    
        // this.resourcePatternResolver = getResourcePatternResolver(); // 初始resourcePatternResolver （父类构造方法）
        org.springframework.context.support.AbstractApplicationContext.AbstractApplicationContext()
        // this.beanFactory = new DefaultListableBeanFactory();   // 创建beanFactory （父类构造方法）
        org.springframework.context.support.GenericApplicationContext.GenericApplicationContext()
        
        // 创建一个用于读取  注解bean  的读取器
        this.reader = new AnnotatedBeanDefinitionReader(this);
            // 最重要的是AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry)部分,做了以下事情
            // 1. 添加对bean进行排序的对象 -beanFactory.setDependencyComparator(AnnotationAwareOrderComparator.INSTANCE); 
            // 2. 添加用于处理延迟加载的对象 -beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());    
            // 3. 手动向容器(this.beanFactory)注册一些实用的内部工具类的BeanDefinition信息，如
            //     AutowiredAnnotationBeanPostProcessor,ConfigurationClassPostProcessor,CommonAnnotationBeanPostProcessor
            //     PersistenceAnnotationBeanPostProcessor,EventListenerMethodProcessor,DefaultEventListenerFactory
            org.springframework.context.annotation.AnnotatedBeanDefinitionReader.AnnotatedBeanDefinitionReader(BeanDefinitionRegistry,Environment)
        // 创建一个用于扫描指定类路径的类
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    // this.reader.register(componentClasses); // 注册参数bean
    2. org.springframework.context.annotation.AnnotationConfigApplicationContext.register
    // 
    3. org.springframework.context.support.AbstractApplicationContext.refresh
```


# spring初始化中的一些明星类和接口
* AnnotatedBeanDefinitionReader
    > * 注释类读取器 - spring中最重要的类
    > * 
* ConfigurationClassPostProcessor
    > * spring 手动注册的6大类之一<br/>
    > * 在创建注释读取器时`this.reader=new AnnotatedBeanDefinitionReader(this);`手动将其BeanDefinition加到工厂中<br/>
    > * 该类实现了`BeanDefinitionRegistryPostProcessor`接口，在spring刷新上下文时会被回调<br/>
    > * 完成对工厂中加了`@Configuration`注解的所有`AnnotatedBeanDefinition`类进行解析，并转成`BeanDefinition`<br/>
    > * 简单说就是解析加了`@Configuration`注解的配置类。如将`@Bean`方法或`@Import`中的类,`@ComponentScans,@ComponentScan`注解路径下加了`@Component`的类 转成`BeanDefinition`。将`@ImportResource`注解中指定的资源配置的bean配置中的类转成`BeanDefinition`
* ImportBeanDefinitionRegistrar
    > * 可以干预beanFactory的建设,必须通过`@Import`注解注册到工厂中
    > * `spring`初始化时`ConfigurationClassPostProcessor`会解析所有`@Import`注解中的类，随后会实例化所有`ImportBeanDefinitionRegistrar`的实现类，并回调接口方法。实现类不会被转成`BeanDefinition`加在工厂中
    > * 实现该接口可以获取到BeanDefinitionRegistrar对象，及加了@Configuration注解的配置类上所有注解元数据(AnnotationMetadata)信息
    > * 有了BeanDefinitionRegistrar就可以向工厂添加、修改、删除BeanDeinition
    > * 拿到的所有注解元数据中，包含了加在@Configuration配置类上的自定义注解，有了BeanDefinitionRegistrar对象，再根据自定义注解的值，可以动态的创建一些BeanDefinition添加到工厂中。如`Mybytis`的`MapperScannerRegistrar`就是扫描了`@MapperScan`注解指定包名下的接口，并创建了一些动态代理类的`BeanDefinition`加到工厂中
* ImportSelector
    > * 可以干预beanFactory的建设,必须通过`@Import`注解注册到工厂中
    > * `spring`初始化时`ConfigurationClassPostProcessor`会解析所有`@Import`注解中的类，随后会实例化所有`ImportSelector`的实现类，并回调接口方法。接口可以返回一组全类名，这些类型会被转成`BeanDefinition`加在工厂中
    