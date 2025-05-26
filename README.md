# AssignmentPart2

This is a Java application that provides a graphical registration, login, and
messaging system using JOptionPane dialogs. It includes input validation and message
storage in JSON format.

Main Features
- User Registration:
• Username must include an underscore (_) and be no more than 5 characters.
• Password must be at least 8 characters long and include a capital letter, a number, and a
special character.
• Cell number must start with +27 and be 12 characters long.
- User Login:
• Authenticates using the registered username and password.
• Welcomes the user if credentials match.
- Messaging:
• Users specify how many messages they want to send.
• Each message is validated and limited to 250 characters.
• Options: Send, Disregard, or Store the message.
- JSON Storage:
• Messages stored;Store&#39; option are saved;messages.json; file.
- Custom GUI Styling:
• Dialog boxes are styled using UIManager with custom font and background color.
- Message Hashing:
• A message hash is auto-generated using the first two digits of the ID, message count, and
the first and last words of the message.
