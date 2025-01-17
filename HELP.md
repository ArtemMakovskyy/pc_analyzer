??? в дто добавить внутринние связи


Инструкция:
1.  базу нужно загрузить результаты тестов с UserBenchmark.
UserBenchmarkController

1.1. загружаем сначала все процессоры без деталей
   ??? если совпадают не загружаются. Не реализовано
   loadAndParseAndSaveCpusWithoutDetails

1.2. Загружаем детали рейтинга.
   из базы загруженной в первом пункте по линкам подгружаются результаты тестов
   loadAndParseAndAddSpecificationCpusWereCpuSpecificationIsNull()
   Если тестов нет загружает если есть пропускает. Фильтрует по наличию тестов
   ??? 2,1 обновить имеющиеся данные. не ревлизовано
1.3. Загружает в базу все видеокарты с тестами
   loadAndParseAndSaveGpus()

2. В базу нужно загрузить данные с HotLine
   HotlineController
 2.1 parseAndSaveAllCpus
     Загружает процессоры с хотлайн
 2.2 parseAndSaveAllGpus
     Загружает видеокарты с хотлайн
 2.3 updateCpuHotlineWithBenchmarkData
     связывает результат тестов  с User Benchmark c загружеными позициями процессоров
 2.4 updateGpuHotlineWithBenchmarkData
   связывает результат тестов  с User Benchmark c загружеными позициями видеокарт



# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.7/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.7/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

