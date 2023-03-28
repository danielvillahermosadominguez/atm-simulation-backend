package org.example.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When


class StepDef {
    @Given("a dummy given")
    fun a_dummy_given() {
        assert(true)
    }

    @When("a dummy thing happens")
    fun a_dummy_thing_happens() {
        assert(true)
    }

    @Then("a dummy result happens too")
    fun a_dummy_result_happens_too() {
        assert(true)
    }
}