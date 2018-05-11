package com.amazon.mShop.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/cucumber" },
        glue = "com.amazon.mShop.steps",
        features = "classpath:cucumber/myFirstFeature.feature"
)
public class RunTestRoom_Test {
}
