Feature: Welcome Screen

  Scenario: Account
    Given ATM with with the following records
      | Name     | PIN    | Balance | Account Number |
      | John Doe | 012108 | 100     | 11223          |
      | Jane Doe | 932012 | 30      | 112244         |
    When An User try to log with account number '112244' and PIN '932012'
    Then User with account number 112244 is logged


