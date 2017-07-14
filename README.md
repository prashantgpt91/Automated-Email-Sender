The task is as follows:
Imagine we have an Email Queue table in our database with each row representing an email that needs sending.

Table Name: EmailQueue
Columns: id, from_email_address, to_email_address, subject, body (you can also add columns as you wish, but at minimum it should have these columns).

Write a java program that will read from this table and send the emails over an SMTP server (not local unix mail). For the SMTP sending you can use a pre-existing library of your choice, if you wish.

Please take into consideration the following:
a) Speed is of the utmost concern. Imagine we have 1 million emails to send. Your code should not sequentially send one email after another.
b) Your solution should scale. In other words, if we determine the fastest we can send emails from a single server is 300 emails/second I should be able to run the same Java program on another server and be able to send another 300 emails/second.
c) Your code should be careful to ensure that it is not possible for the same email to be sent twice (especially when running multiple processes of the program).
d) Don't worry about whether the SMTP server can handle the load. It can.
e) Order emails are sent is not necessarily important. In other words, even though I'm calling this a "queue" it's not important to guarantee that emails are sent in the order they are queued.
f) Comment your code extensively.
g) Once again, speed is the primary concern. So do remember to think about what the slow points in the email sending process could be and take efforts to minimize those pain points.
h) Send me instructions on how to compile/run/test your code along with the code itself. Also the sql statements for me to create the table that you are working with and the sql statements to insert a few test emails.
