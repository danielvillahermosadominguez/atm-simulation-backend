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
