Feature: Welcome Screen

  Scenario: Account
    Given ATM with with the following records
      | Name     | PIN    | Balance | Account Number |
      | John Doe | 012108 | 100     | 11223          |
      | Jane Doe | 932012 | 30      | 112244         |
    When An User try to log with account number '112244' and PIN '932012'
    Then User with account number 112244 is logged

  Scenario Outline: Problem with the login
    Given ATM ready to be used
    When An User try to log with account number <account_number> and PIN <PIN>
    Then the user should see the message <error>

    Examples:
      | account_number | PIN      | error                                                                    |
      | '11224'        | '932012' | 'Account Number should have 6 digits length for invalid Account Number'  |
      | '1122a'        | '932012' | 'Account Number should only contains numbers for invalid Account Number' |
      | '112245'       | '9320'   | 'PIN should have 6 digits length for invalid PIN'                        |
      | '112245'       | '9320a'  | 'PIN should only contains numbers for invalid PIN'                       |


  Scenario Outline: Account does not exists
    Given ATM with with the following records
      | Name     | PIN    | Balance | Account Number |
      | John Doe | 012108 | 100     | 11223          |
      | Jane Doe | 932012 | 30      | 112244         |
    When An User try to log with account number <account_number> and PIN <PIN>
    Then the user should see the message <error>

    Examples:
      | account_number | PIN      | error                                                                    |
      | '112245'       | '932012' | 'Invalid Account Number/PIN'  |
      | '932012'       | '123456' | 'Invalid Account Number/PIN'  |
