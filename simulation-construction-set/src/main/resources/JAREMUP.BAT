set path="C:\jbuilder7\jdk1.3.1\bin";%path
jar cf0 simulationconstructionset.jar com
copy simulationconstructionset.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\jars\lib\ext"
C:
cd C:\MySimulations\SpringFlamingo\classes
jar cmf0 SpringFlamingoManifest SpringFlamingo.jar springflamingo
copy SpringFlamingo.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
cd C:\MySimulations\Trebuchet\classes
jar cmf0 TrebuchetManifest Trebuchet.jar trebuchet
copy Trebuchet.jar "E:\My  Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
cd C:\MySimulations\FallingBrick\classes
jar cmf0 FallingBrickManifest FallingBrick.jar fallingbrick
copy FallingBrick.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
cd C:\MySimulations\Mobile\classes
jar cmf0 MobileManifest Mobile.jar mobile
copy Mobile.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
cd C:\MySimulations\AgileHexapod\classes
jar cmf0 AgileHexapodManifest AgileHexapod.jar agilehexapod
copy AgileHexapod.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
cd C:\MySimulations\StewartPlatform\classes
jar cmf0 StewartPlatformManifest StewartPlatform.jar stewartplatform
copy StewartPlatform.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
cd C:\MySimulations\M2\classes
jar cmf0 M2Manifest M2.jar m2 com
copy M2.jar "E:\My Documents\yobotics\Products\SimulationConstructionSet\deliveries\InstallShield\demos"
e: