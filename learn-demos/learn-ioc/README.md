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
       
    
    
    
# AnnotationConfigApplicationContext初始化调用链
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


# spring初始化中的一些明星类

    


    
    