# Requirements #

- java 17 or greater
- For database logging MySQL or Postgres database engine connection

# USAGE #

For maven

    <dependency>
        <groupId>org.makechtec.software</groupId>
        <artifactId>amatl</artifactId>
        <version>1.0.13</version>
    </dependency>

For gradle groovy

    implementation 'org.makechtec.software:amatl:1.0.13'

For gradle kotlin

    implementation("org.makechtec.software:amatl:1.0.13")

## Filesystem logging ##

    var mainConfig = new FilesystemOutput(new FileGenerationSettings(
            1000,
            FrequencyUnit.YEAR,
            new SimpleTimestampNameStrategy(),
            new NameSettings(
                    "/Users/estefaniarv/Desktop/angel/development/static-for-tests/logging",
                    "log-",
                    ".txt"
            )
    ));

    var log = new Cuicatl(new LevelOutputSettings(
            mainConfig,
            mainConfig,
            mainConfig,
            mainConfig,
            mainConfig
    ));

    log.info("hello filesystem {}", "filesystem-admin");

## In memory logging ##

    var messages = new ArrayList<String>();

    var log = new Cuicatl(new LevelOutputSettings(
            new InMemoryOutput(messages),
            new InMemoryOutput(messages),
            new InMemoryOutput(messages),
            new InMemoryOutput(messages),
            new InMemoryOutput(messages)
    ));

    log.info("hello {}", "angel");

    messages.forEach(System.out::println);

## Streaming logging ##

    var log = new Cuicatl(new LevelOutputSettings(
            new StreamOutput(System.out),
            new StreamOutput(System.out),
            new StreamOutput(System.out),
            new StreamOutput(System.out),
            new StreamOutput(System.out)
    ));

    log.info("hello streaming with {}", "angel");


## Debug ##

    ...
    log.info("hello streaming with {}", "angel");

    if(log.hasException()){
        var originalException = log.getOriginalException();

        e.printStackTrace();
    }
