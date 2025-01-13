# Simple Spreadsheet Project

Welcome to my Simple Spreadsheet project! This is a sample project that demonstrates how to build a simple spreadsheet application in Java.


## Project Description

This project implements a simple spreadsheet that includes features such as storing values in cells, evaluating formulas, and performing arithmetic operations. The spreadsheet allows the user to enter different types of values into the cells as follows:

1. **Text**:
   - When the user enters text in a cell, the text will appear as is in that cell.

2. **Number**:
   - When the user enters a number, the number will appear in the cell in double format (for example, if the number 10 is entered, 10.0 will appear in the cell).

3. **Formula**:
   - Any formula starts with the "=" sign.
   - After the "=", the user can write a number, a mathematical formula, or refer to another cell containing a value.
   - This allows the cell to be dependent on the value in another cell that it references.


## Class Documentation

### Ex2Sheet
Manages the main spreadsheet, including operations such as calculating values in cells, saving and loading data from files, and checking the validity of cell positions.

### CellEntry
Implements an interface that returns invalid values for X and Y and indicates if the value in the cell is valid.

### SCell
Represents a single cell in the spreadsheet, storing values of different types (text, number, formula) and includes information about its type.
![ללא שם](https://github.com/user-attachments/assets/02cbe73b-6ee3-44cf-956d-9a96092c40d7)
