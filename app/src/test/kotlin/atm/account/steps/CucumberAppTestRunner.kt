package atm.account.steps

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

//https://github.com/jecklgamis/cucumber-jvm-kotlin-example/blob/main/src/test/kotlin/runner/ExampleFeatureTest.kt

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["classpath:cucumber/features/welcome.feature"],
    glue = ["classpath:atm/account/steps"],
    plugin = ["pretty", "json:target/jsonReports/features.json", "html:target/cucumber/html", "html:target/cucumber/features.html"]
)
class CucumberAppTestRunner
