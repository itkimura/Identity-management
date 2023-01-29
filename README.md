# Identity-management
My Java solution for programming task of Summer Trainee 2023: Software Developer in Visma Solutions

**Problem:**

Visma Solutions Oy is developing an application for identity management. With the application, users can login to integrated applications, confirm payments and sign documents. Identity management is a mobile application, and other apps can call its services through deep linking. When this is used, the identity management application would open automatically from the right in-app location. In this assignment, you will be implementing a part of this logic.


## Description

I understand that the solution for this problem
1. Validation of URI 
- validate URI format
- validate scheme and path
2. Parse stirng into object (I used Java <a href="https://docs.oracle.com/javase/7/docs/api/java/net/URI.html">class URI</a> instead of parsing everything manually)
3. Validate requirements 
- login:source(type:string)
- confirm:source(type:string)&payment number(type:integer)
- sign:source(type: string)&documentid(type:string)
4. return path and parameters when input is valid or throw exceptions.


## Challenges
1. I built the first solution without the data class and failed with some tests. So I added new classes for returning path and parameters and also parameter class, it now handles errors and does not cause exceptions.  
2. There were many conditional expressions which are hard to understand by others. So I decided to add separate function to test requirements for queries. It looks simpler and readable.
3. I was not sure about the requirement of client ```"Implementation needs to have a client, which uses the new class. You can for example implement the client as another class that uses the relevant methods"``` So I made the client which uses my identity management, however there are improvement possibilities.

## Possible Improvement

1. The example URIs is missing authority (user information & host) info. My solution can handle when the authority is missing. If the URI contains authority, need to improve parsing. Example:


  <img src="https://user-images.githubusercontent.com/61685238/215346655-b3c71357-4590-4e1a-a77a-67afdecb7ce4.jpg" width="600"></img>



2. The client doesn't log errors. It would be better to log errors.
3. The client should be more configurable. At the moment, it is hard coded base URI.
