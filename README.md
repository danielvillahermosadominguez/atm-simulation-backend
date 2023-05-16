# atm-simulation-backend
https://github.com/codurance/atm-simulation-exercise/blob/main/docs/Stage-I/ATM-Simulation.md
# 28-04-2023 - Decisions:
* We think about the testing strategy. This is in the Miro board. Basicly:
  * BDD -> outside in
  * We are not going to start the outside-in in the service layer. We want to avoid testing of the terminal output
    * because, we want to evolve to a front-end -> backend app, and the console ui will be temporal
    * We use a facade to connect the acceptance tests to the service, because in this way we will add more layers (for example http - layer) with minimum effort 
    
# 05-05-2023 - Decisions:
For the first iteration we want to:
* [ ] Finish the requirements with this design and acceptance test 
* [x] Surely, persistence will be with a file. (To decide)
* [ ] Console program implementation: it will be basic and without acceptance tests, but we could use unit tests.
* [ ] Basic pipeline with compilation and tests

NOTE: We could think to test the console (Terminal output) using this, as a layer more.
https://blog.mestwin.net/test-the-console-output-println-in-kotlin/

# 16-05-2023
For the first iteration we want to:
* [ ] Finish the requirements with this design and acceptance test
    * [ ] For this exercise we want to apply the concept of Either to controll errors !!!
    * [ ] Account Number should have 6 digits length. Display message Account Number should have 6 digits length for invalid Account Number.
    * [ ] Account Number should only contains numbers [0-9]. Display message Account Number should only contains numbers for invalid Account Number.
    * [ ] PIN should have 6 digits length. Display message PIN should have 6 digits length for invalid PIN.
    * [ ] PIN should only contains numbers [0-9]. Display message PIN should only contains numbers for invalid PIN.
    * [ ] Check valid Account Number & PIN with ATM records. Display message Invalid Account Number/PIN if records is not exist.
    * [ ] Valid Account Number & PIN will take user to Transaction Screen.
* [x] Surely, persistence will be with a file. (To decide)
* [ ] Console program implementation: it will be basic and without acceptance tests, but we could use unit tests.
* [ ] Basic pipeline with compilation and tests
