
# Problem Statement: 

Imagine we have an Email Queue table in our database with each row representing an email that needs sending.

Table Name: EmailQueue
Columns: id, from_email_address, to_email_address, subject, body (you can also add columns as you wish, but at minimum it should have these columns).

Write a java program that will read from this table and send the emails over an SMTP server (not local unix mail). For the SMTP sending you can use a pre-existing library of your choice, if you wish.

Please take into consideration the following:
-  Speed is of the utmost concern. Imagine we have 1 million emails to send. Code should not sequentially send one email after another.
-  Solution should scale. In other words, if we determine the fastest we can send emails from a single server is 300 emails/second I should be able to run the same Java program on another server and be able to send another 300 emails/second.
-  Be Careful to ensure that it is not possible for the same email to be sent twice (especially when running multiple processes of the program).
-  No need to worry about whether the SMTP server can handle the load. It can.
-  Order in which emails are sent is not necessarily important. In other words, even though I'm calling this a "queue" it's not important to guarantee that emails are sent in the order they are queued.



## Approach-1 | Flow Diagram


<img src="https://github.com/x0v/Automated-Email-Sender/blob/master/Approach-1.jpg" width="1000">





## Approach-2 | Flow Diagram


<img src="https://github.com/x0v/Automated-Email-Sender/blob/master/Approach-2.jpg" width="1000">

