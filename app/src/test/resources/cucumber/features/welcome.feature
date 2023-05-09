Feature: Welcome Screen

  Scenario: Account
    Given ATM with with the following records
      | Name      | PIN     | Balance | Account Number |
      | John Doe  | 012108  | 100     | 11223          |
    When An User try to log with account number '11223' and PIN '012108'
    Then User with account number 11223 is logged