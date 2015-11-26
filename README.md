# domain-registrar #

## Libraries/ dependencies ##
* Java 1.8
* Guice 4.0
* log4j 1.2
* Maven 3.2
* Junit 4.11
* mockito 1.9

Maven is used as build tool in this project, but even without maven it can be used.

## Design ##
1. `DomainDao --> DomainDaoBasicImpl` :: 
Dao layer is been used to save and retrieve domain name prices and details. But haven't used any persistence data store. Within the basic dao implementation I am keeping domain name details inside ArrayList.
2. `DomainRegistrarService --> DomainRegistrarServiceImpl` ::
Service layer implements all business login from reading file, reading console arguments and then calculate total amount for the list of domain names found in given file and arguments 
3. `App`
App is the main class and starting point for the application. It will get all console program arguments and pass them to service layer for processing and calculation.
4. All these components are glued together using guice di framework. Doa layer is inject to service layer and service later injected to main class. Main class initialize guice with program start. 

## Important Classes ##
* App -- main class
* DomainRegistrarServiceImpl -- read file and console arguments and calculate total price
* DomainDaoBasicImpl -- create and retrieve domain details records

## Test Cases ##
Test cases can be found under `src/test/java`. Test coverage is more than 80%.
* DomainDaoBasicImplTest
* DomainRegistrarServiceImplTest

## How to run this ##
1. Clone the git the project from GitHub or download zip.
2. `mvn clean install` to build project, `mvn test` to run test cases and `mvn eclipse:eclipse` to create eclipse project files. You can directly import it to eclipse.
3. Run it command line. </br>
`-n` :: pass domain name and number of years separate by a comma. **eg: '-n thisara.com.au,2'** </br>
`-p` :: pass CSV file with domain name, no of year key value pairs. **eg: '-p resources/domain-names.csv'** </br>
This path could be absolute or relative inside the artifact. format of the file contect will be as follows </br>
`a-domain.com,1` </br>
`another-domain.net,2`
4. Any combination of above `-n, -p` switches can be used to run the program and it will consider all parameter and the given file for calculation.
</br>

	`java -cp target/domain-registrar-1.0.0-jar-with-dependencies.jar org.thisarattr.domain.registrar.App -p resources/domain-names.csv -n a-domain.com,1`

## Assumptions ##
1. Use number of years as double to cater 1/2 years for fraction of years.
2. Assume input file format as CSV. `"a-domain.com,1"` as a single line
3. Application log file will be creates at `/data/logs/domain-registrar.log`
4. Mockito is used to mock dependencies while testing.
