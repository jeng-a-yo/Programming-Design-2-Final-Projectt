# Attendance System

This project implements an attendance system in Java. The system involves distributing tokens to users who are present, and the users must then email their tokens back to a central email account. The system will verify the tokens to determine whether the users have successfully marked their attendance. Users are allowed one mistake; if they send an incorrect token twice, they will be marked as absent.

## Features

- Generate and distribute tokens to present users.
- Users reply with their tokens via email.
- Verify the correctness of the received tokens.
- Allow one mistake; mark as absent on the second incorrect token.

## Usage

1. **Generate Tokens:**

    Implement a method to generate unique tokens for each user who is present.

2. **Send Tokens via Email:**

    Use JavaMail API to send emails containing tokens to users.

3. **Receive and Verify Tokens:**

    Periodically check the central email account for replies. Parse the emails to extract tokens and verify them.

4. **Mark Attendance:**

    Keep track of users and their token submissions. Allow one mistake, and mark as absent on the second incorrect submission.

## Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

