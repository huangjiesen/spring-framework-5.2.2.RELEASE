# Spring AOP
与OOP对比，面向切面，传统的OOP开发中的代码逻辑是自上而下的，而这些过程会产生一些横切性问题，这些横切性的问题和我们的主业务逻辑关系不大，这些横切性问题不会影响到主逻辑实现的，但是会散落到代码的各个部分，难以维护。AOP是处理一些横切性问题，AOP的编程思想就是把这些问题和主业务逻辑分开，达到与主业务逻辑解耦的目的。使代码的重用性和开发效率更高。

AOP的典型应用场景如：日志记录，权限验证，事务管理，效率检查，exception


## [Spring AOP相关术语](https://docs.spring.io/spring/docs/5.1.9.RELEASE/spring-framework-reference/core.html#aop-introduction-defn)
* Aspect(切面):
* Join point(连接点): 要切入的目标方法，如被切入事务的service层方法
* Advice(增强): 切面在连接点处要执行的操作。`Advice`类型包含`Before`、`Pointcut`、`After`等。包括Spring在内的许多AOP框架都将`Advice`封装为拦截器，并围绕连接点维护一系列拦截器
* Pointcut(切入点): 匹配连接点的一个称谓,其实就是匹配连接点的一个表达式，通过切入点匹配到`连接点`进行`Advice`，默认情况下，Spring使用AspectJ切入点表达语言。
* Introduction(介绍): 
* Target object(目标对象): 被`Advice`的原始对象，也称为`advice object`,由于Spring AOP是使用运行时代理实现的，因此该对象始终是代理对象。
* AOP proxy(代理对象): 由AOP框架创建的一个对象，包含了目标对象代码及增强逻辑代码的对象。
* Weaving(织入): 把`advice`逻辑加入到`连接点`的过程叫做织入。这可以在编译时（例如，使用AspectJ编译器），加载时或在运行时完成。像其他纯Java AOP框架一样，Spring AOP在运行时执行织入。

## [Spring AOP包含以下几种Advice(增强)类型]
* Before advice: 在切入点开始处切入内容,但是它不能阻止执行流程继续进行到连接点（除非它引发异常）。
* After returning advice: 在切入点结尾处切入内容
* After throwing advice: 如果方法因抛出异常而退出，则执行
* After (finally) advice: 无论连接点退出的方式如何（正常或异常返回），均应执行建议。
* Around advice: 在切入点前后切入内容，围绕增强可以在方法调用之前和之后执行自定义行为。它还负责选择是返回连接点还是通过返回其自身的返回值或引发异常来增强方法执行。

## Spring Aop和AspectJ的关系
Aop是一种编程概念，SpringAop、AspectJ都是Aop的实现，SpringAop有自己的语法，但是语法复杂，所以SpringAop借助了AspectJ的注解，但是底层实现还是自己的.