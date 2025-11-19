# Establishment Service
# Running tests locally
## REST Integration Tests
1. Make sure you have Java17 installed
1. Open the `establishment-rest-service-test` project in IntelliJ
1. On the top menu bar, click the drop down menu that says 'current file'
1. Click on 'Edit Configurations'
1. Click on the '+' icon in the top left hand corner to add a new configuration
1. Click 'JUnit'
1. Enter the following config:
    - Name: EstablishmentRestTests
    - Change 'Class' to 'all in package'
    - Where it says `-ea` add the following config:
``` 
-D"dbunit.connectionUrl=jdbc:oracle:thin:@//localhost:1521/xepdb"
-D"dbunit.username=ISISUSERDB"
-D"dbunit.password=pa55w0rdTolocalDB"
-D"userOfficeTest.restUri=http://localhost:4019"
-D"uk.stfc.bisapps.config=C:\FBS\Apps\secret-config\local\establishment-service\RestTest.config"`
```

9. Click ok
10. Select the config on the top menu bar
11. Click the green play button to run the tests
