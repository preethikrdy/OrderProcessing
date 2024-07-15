# Orders Processor

This repository contains a Java application for processing customer orders from data files. The system supports both single-threaded and multi-threaded processing of orders, reading item data, and writing summarized results to a file.

## Overview

The application processes orders by reading item data from a specified file, processing the orders (either sequentially or in parallel using multiple threads), and writing the results to a specified output file.

## Classes Description

### OrdersProcessor
The main class of the application. It handles user input, reads item data from a file, processes customer orders using either single-threaded or multi-threaded processing based on user input, and writes the results to an output file.

### Order
Represents an individual customer's order. This class implements the `Runnable` interface to support multi-threaded processing. It reads order details from a file, calculates the total cost, and provides a summary of the order.

## Key Java Concepts Used

- **File I/O**: Reading from and writing to files using `Scanner`, `File`, and `FileWriter`.
- **Collections**: Using `TreeMap` for storing and sorting item data and customer orders.
- **Concurrency**: Implementing multi-threaded processing using the `Runnable` interface and `Thread` class.
- **Synchronization**: Ensuring thread-safe access to shared resources with the `synchronized` block.
- **StringBuilder**: Efficiently building and formatting strings for output.
