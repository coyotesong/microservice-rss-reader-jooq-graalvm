# RSS Reader Microservice

This repository contains a microservice implements an RSS client that can import
standard OPML files and periodically scan the referenced RSS feeds for new content.

The runtime code is framework-agnostic - it can be used with either Spring Boot
or Gradle. Spring Boot Native is getting better but Graalvm support is still better
with Gradle.

### Target Audience

There are two target audiences:

- Potential employers who want a non-trivial demonstration of my competence with different technologies
- Other developers who want to experiment with different technologies

This microservice is also part of a larger effort to implement a search engine that indexes a curated
list of sites and allows you to filter results to the recent past.

A search engine for java developers might index sites like

- [Baeldung](https://www.baeldung.com/) - tutorials
- [DZone/java](https://dzone.com/java) - news
- [jOOQ blog](https://blog.jooq.org/) - news and tutorials

while a search engine for Linux users might index a site like

- [RedHat blog](https://www.redhat.com/en/blog) 
- [OMG Ubuntu](https://www.omgubuntu.co.uk/) - news

This dedicated search engine would probably ignore the usual sites like StackOverflow due to the
amount of noise and outdated information on the site.

## Architecture

The architecture is designed around security and vulnerability analysis. The `core` module is
very light - it only includes the data model and MVC interfaces.

- **core** - data model and MVC interfaces
- **rss-client** - all RSS-specific code.
- **persistence** - all database-specific code. Current implementation is jOOQ but with plans to provide alternative implementations.
- **integrations** - integration with Spring and Gradle
- **applications** - all applications

The RSS-client and persistence modules are integrated into the Spring or Gradle frameworks via the
[ServiceLoader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) mechanism.
The **core** module includes both `RssServiceFactory` and `RssRepositoryFactoryFactory`
SPI definitions and the implementations specify them via a file under `/META-INF/services`.
This allows the respective frameworks to continue to use auto-configuration since they can
contain stubs that use an auto-configured factory for initialization.

(Note: the persistence SPI is a FactoryFactory since we need to provide a
[DataSource](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/ServiceLoader.html) that is
created by the application.)

