https://github.com/codurance/atm-simulation-exercise/tree/main

# 09-06-2023
For the first iteration we want to:
* [ ] Finish the requirements with this design and acceptance test
    * [x] For this exercise we want to apply the concept of Either to controll errors !!!
    * [x] Account Number should have 6 digits length. Display message Account Number should have 6 digits length for invalid Account Number.
    * [x] Account Number should only contains numbers [0-9]. Display message Account Number should only contains numbers for invalid Account Number.
    * [X] PIN should have 6 digits length. Display message PIN should have 6 digits length for invalid PIN.
    * [X] PIN should only contains numbers [0-9]. Display message PIN should only contains numbers for invalid PIN.
    * [X] Check valid Account Number & PIN with ATM records. Display message Invalid Account Number/PIN if records is not exist.
    * [ ] Valid Account Number & PIN will take user to Transaction Screen. 
* [x] Surely, persistence will be with a file. (To decide)
* [ ] Console program implementation: it will be basic and without acceptance tests, but we could use unit tests.
  * Explorar alguna librería de "bus" / event source approach
* [x] Basic pipeline with compilation and tests

Ahora nos encontramos tratando de diseñar la consola y como vamos a trabajar con ella.

- En principio pensamos que la consola será un objeto con el que interactuaremos. Algunas 
  ideas:
  - La consola escribira por pantalla los mensajes. Eso lo hemos testeado en otros casos
  - La consola podria recibir mensajes del servicio y el servicio contestarle asincronamente,
    por lo que ¿como lo haria? colas?
  - ¿como va a interactuar el usuario con la consola?
    - Quiza user-> console -> commands <- adapter -> service
      - y service->notification->adapter->notificationInterface->console
        https://www.slideshare.net/jonsimon2/command-and-adapter-pattern
    - 

Vamos explorando como comunicarnos con la consola:
  - Channels
  - Corutinas de kotlin


