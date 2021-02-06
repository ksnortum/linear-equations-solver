# linear-equations-solver

This is a command-line program to solve a systems of linear equations. Input and output is managed by text files, 
although diagnostic row manipulations and the solution are printed to the screen.

## Launching

While in the root of the project (the same level as `Main.java` is in), execute a command similar to the following:

    java Main -in <input file name> -out <output file name>

*TODO* explain about compiling?

### Structure of input file

The first line of the input file should contain two integers separated by whitespace.  The first integer is the
number of variables in the equations and the second is the number of equations.  Following that should be rows
of the coefficients only of each equation.  If a variable is missing from the equations, use `0` as the
coeficient.  Coeficients can be complex numbers in this form:

    a+bi

or

    a-bi
    
...where `a` and `b` are real numbers.  Notice that there must not be any whitespace between the real and the
imaginary part of the complex number.

### Structure of the output file

After executing the program, the output will contain one of three things:

* The text, "There are no solutions"
* The text, "There are infinitely many solutions"
* The solution, one variable per line

### Credit

This is a project from [Hyperskill](https://hyperskill.org).
