# PP_CW1
***Programming paradigms coursework 1***

A scala app that can interprit data from text file. 

This application can do 6 primary functions:
	- Get most recent prices
 	- Get the highest and lowest prices
	- Get the median price
 	- Get which food item has risen the most
	- Show averages of all food items, and allow user to compare 2
 	- Allow user to add items to a food basket, with most recent prices, and display the cost. 

 Compulsory preconditions:
 	- App menu must invoke functions to perform required operations.
	- Some functions must require user input at console.
 	- Compose functions: 
		- Processes data to return Map[String, Int]. 
		- Displays results by iterating through the map.

 Current bugs:
 	- While the first menu uses recursion, whole app is not recursive, meaning a lot of ctrl+f5.
	- Some item prices are displayed to 2 decimal places, which is weird as food items are priced in pence, not pounds / pence.
 	- Basket menu can not add same item twice, irrespective of amount. 
	- Food item selection must be entered in UPPERCASE or it is not recognised. 

 Future development suggestions:
 	- GUI 
	
