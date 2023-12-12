import java.util.InputMismatchException
import scala.io.Source
import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer


object CW1 extends App {
  /*
   1. Get current (most recent) price for each food.
  2. Get the highest and lowest prices within the period for each food.
  3. Get the median price over the period for each food.
  4. Get the symbol for the food which has risen most over the last 6 months, that is, has shown
  the largest rise between the last 6 months of the period and the 6 months earlier.
  5. Compare the average values over the 2-year period of two foods selected by the user.
  6. Allow the user to input a food basket and show its total value based on the current (most
  recent) values of the foods.
  • Each basket entry is a food symbol and the number of kg/litres (as a float value).
  • The basket should be stored in another map structure, e.g. Map("RICE" -> 2.5,
  "BEEF" -> 0.5, "APPLE" -> 1).
  • Any food symbol not recognized will be ignored (and a warning message printed).
    */

  var input: Int = 0

  do {
    println("[1] Current Prices\n" +
      "[2] Highest and Lowest Prices\n" +
      "[3] Median Prices\n" +
      "[4] Risen Most in 6 Months\n" +
      "[5] Compare Averages of Two\n" +
      "[6] Basket\n" +
      "Enter your option: ")

    //get input
    input = Source.stdin.getLines().next().toInt

    if (input == 1) {
      current()
    } else if (input == 2) {
      highLow()
    } else if (input == 3) {
      median()
    } else if (input == 4) {
      risen()
    } else if (input == 5) {
      averages()
    } else if (input == 6) {
      basket()
    } else {
      println("That's not an appropriate option.")
    }

  } while (input < 1 || input > 6) //CALL RECURSION


  //**********************LOGIC FOR APP*******************************
  //option 1
  def current(): Unit = {
    //get access
    val datafile = "src/data.txt"
    //source to read
    val foodData = Source.fromFile(datafile).mkString

    //split into lines
    val lines = foodData.split("\n")

    //get most recent price after split
    lines.foreach { line =>
      val items = line.split(", ")
      val foodItem = items.head
      val currentPrice = items.last

      //print
      println(s"Food item: $foodItem Current Price: $currentPrice")
    }
  }

  //option 2
  def highLow(): Unit = {
    //get access
    val datafile = "src/data.txt"
    //source to read
    val foodData = Source.fromFile(datafile).mkString

    //split into lines
    val lines = foodData.split("\n")

    // use listmap to store associations in data
    var foodItemPrices = ListMap[String, List[Int]]()

    //fill listmap with food items
    lines.foreach { line =>
      val items = line.split(", ")
      val foodItem = items.head
      val prices = items.tail.flatMap { price =>
        try {
          Some(price.toInt)
        } catch {
          case _: NumberFormatException => None
        }
      }.toList

      foodItemPrices += (foodItem -> prices)
    }

    // iterate for highest and lowest
    foodItemPrices.foreach { case (foodItem, _) =>
      //get prices
      val prices = foodItemPrices(foodItem)
      if (prices.nonEmpty) {
        // now sort
        val sortedPricesForItem = prices.sorted
        // get hidgest and lowest
        val highest = sortedPricesForItem.last
        val lowest = sortedPricesForItem.head

        // Print the results
        println(s"Food item: $foodItem, Highest Price: $highest, Lowest Price: $lowest")
      }
    }
  }

  //option 3
  def median(): Unit = {
    //get access
    val datafile = "src/data.txt"
    //source to read
    val foodData = Source.fromFile(datafile).mkString

    //split into lines
    val lines = foodData.split("\n")

    // create map
    var foodItemPrices = Map[String, List[Int]]()

    // put prices from data into map
    lines.foreach { line =>
      val items = line.split(", ")
      val foodItem = items.head
      val prices = items.tail.flatMap { price =>
        try {
          Some(price.toInt)
        } catch {
          case _: NumberFormatException => None
        }
      }.toList

      foodItemPrices += (foodItem -> prices)
    }

    // iterate
    foodItemPrices.foreach { case (foodItem, _) =>
      // get prices
      val prices = foodItemPrices(foodItem)
      if (prices.nonEmpty) {
        // sort prices
        val sortedPricesForItem = prices.sorted

        // calculate median
        val median = {
          val n = sortedPricesForItem.length
          if (n % 2 == 0) {
            val mid = n / 2
            (sortedPricesForItem(mid - 1) + sortedPricesForItem(mid)) / 2.0
          } else {
            sortedPricesForItem(n / 2)
          }
        }

        // print
        println(s"Food item: $foodItem, Median Price: $median")
      }
    }
  }

  //option 4
  def risen(): Unit = {
    //get access
    val datafile = "src/data.txt"
    //source to read
    val foodData = Source.fromFile(datafile).mkString

    //split into lines
    val lines = foodData.split("\n")

    //create map
    var foodItemPrices = Map[String, List[Int]]()

    //fill map with prices
    lines.foreach { line =>
      val items = line.split(", ")
      val foodItem = items.head
      val prices = items.tail.flatMap { price =>
        try {
          Some(price.toInt)
        } catch {
          case _: NumberFormatException => None
        }
      }.toList

      foodItemPrices += (foodItem -> prices)
    }

    // calculate percentage increase
    val percentageIncreases = foodItemPrices.mapValues { prices =>
      val recentPrice = prices.last
      val sixMonthsAgoPrice = prices.drop(6).headOption.getOrElse(0)
      if (sixMonthsAgoPrice != 0) {
        ((recentPrice - sixMonthsAgoPrice).toDouble / sixMonthsAgoPrice) * 100
      } else {
        //if no value from 6 months prior assign 0 - but also helps code work
        0.0
      }
    }

    //find item with highest increase
    val maxIncreaseFoodItem = percentageIncreases.maxBy(_._2)

    // print
    println(s"The food item that has increased the most in the last 6 months is: ${maxIncreaseFoodItem._1} " +
      s"\nIt has increased by ${maxIncreaseFoodItem._2}%")
  }

  //option 5
  def averages(): Unit = {
    //get access
    val datafile = "src/data.txt"
    //source to read
    val foodData = Source.fromFile(datafile).mkString

    //split into lines
    val lines = foodData.split("\n")

    // create map
    var foodItemPrices = Map[String, List[Int]]()

    // put data into map
    lines.foreach { line =>
      val items = line.split(", ")
      val foodItem = items.head
      val prices = items.tail.flatMap { price =>
        try {
          Some(price.toInt)
        } catch {
          case _: NumberFormatException => None
        }
      }.toList

      foodItemPrices += (foodItem -> prices)
    }

    // menu
    println("Select two food items to compare averages (UPPERCASE):")
    foodItemPrices.keys.toList.sorted.foreach(println)

    //user input
    print("Enter the first food item: ")
    val foodItem1 = scala.io.Source.stdin.getLines().next()

    print("Enter the second food item: ")
    val foodItem2 = scala.io.Source.stdin.getLines().next()

    // check inputs against data.txt
    if (foodItemPrices.contains(foodItem1) && foodItemPrices.contains(foodItem2)) {
      // calculate averages for selected
      val average1 = foodItemPrices(foodItem1).sum.toDouble / foodItemPrices(foodItem1).length
      val average2 = foodItemPrices(foodItem2).sum.toDouble / foodItemPrices(foodItem2).length

      //format to 2 decimal place because .091209190299 looks super ugly
      val formattedAverage1 = f"$average1%.2f"
      val formattedAverage2 = f"$average2%.2f"

      println(s"Comparison of Averages: \n" +
        s"First Food item: $foodItem1, Average Price: $formattedAverage1 \n" +
        s"Second Food item: $foodItem2, Average Price: $formattedAverage2")

      // check what has higher amount to avoid -minus results
      if (average1 > average2) {
        println(s"$foodItem1 has a higher average price than $foodItem2.")
      } else if (average1 < average2) {
        println(s"$foodItem2 has a higher average price than $foodItem1.")
      } else {
        println(s"$foodItem1 and $foodItem2 have the same average price.")
      }
    } else {
      println("Invalid food items. Please make sure you entered valid food items.")
    }
  }

  //option 6
  def basket(): Unit = {
    // get access
    val datafile = "src/data.txt"
    // source to read
    val foodData = Source.fromFile(datafile).mkString

    // split into lines
    val lines = foodData.split("\n")

    // create a map to store current prices as per diet instruction
    var currentPrices = Map[String, Float]()

    // get most recent price after split
    lines.foreach { line =>
      val items = line.split(", ")
      val foodItem = items.head
      val currentPrice = items.last.toFloat

      // store current price in the map
      currentPrices += (foodItem -> currentPrice)

      // print
      println(s"Food item: $foodItem Current Price: $currentPrice")
    }

    // create a map to store the basket items
    var basket = Map[String, Float]()
    var totalCost = 0.0

    //allow user to keep adding if y
    var keepAdding = true

    while (keepAdding) {
      // get input for basketised items
      println("Which item would you like to add to your basket? (UPPERCASE): ")
      val basketItem = scala.io.Source.stdin.getLines().next()

      // check if the entered item is in the current prices map
      if (currentPrices.contains(basketItem)) {
        // get amount of items in kg/litres
        println("How much of this item would you like? (kg/litres): ")
        val itemAmount: Float = scala.io.Source.stdin.getLines().next().toFloat //use floaf as per diet instruction

        // add the item and its quantity to basket map
        basket += (basketItem -> itemAmount)

        //calculate total cost
        totalCost += itemAmount * currentPrices(basketItem)

        // get true cost
        val trueCost = itemAmount * currentPrices(basketItem)

        println(s"You have chosen: $itemAmount kg/litres of $basketItem, costing $trueCost (pence)")
        println("Item added to basket. Would you like to add another? (y/n)")

        // get user input to continue or not
        val userInput = scala.io.Source.stdin.getLines().next().toLowerCase()
        keepAdding = userInput == "y"
      } else {
        println("Invalid food item. Please make sure you entered a valid food item.")
      }
    }

    // display basket summary with total cost
    println("Basket summary:")
    basket.foreach { case (item, quantity) =>
      println(s"$item: $quantity kg/litres")
    }
    println(s"Total cost: $totalCost")
  }

}