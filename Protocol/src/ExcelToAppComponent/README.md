# Excel to AppComponent

This section allows you to transform an Excel File that describes the solar car's components to the AppComponents in the Java Program without touching the Java Program.

- Each Sheet is a Component. The name of the seet must match the name of the Component
- Each column describes a parameter of the component
- First Row is a string containing the name of the parameter
- Second row describes the minimum value of the parameter (decimals are important)
- Third row describes the maximun value of the parameter (decimals are also important)

Important: the excel file must be named `components.xlsx` and stay in a subdirectory with the name of the car

# How to Excel->CSV

- Install Python 3.7+
- Create a virtual environment with `py -m venv venv`
- Install requirements `pip install -r requirements.txt`
- If you are using Windows, activate it with `.\venv\Scripts\activate`. Otherwise use `source bin/activate` in UNIX
- Make sure that `directory` variable in `Excel_to_CSV.py` targets the directory where the Excel file you want to convert is correct
- Execute the script with `py Excel_to_CSV.py`

Now you will see multiple CSV files in the same path of the Excel you put before. This will be the input for the next step.

NOTE 1: CSV files will be overwritten each time you execute the script.
NOTE 2: There may be problem with linux due to the slashes in directory paths. Just change \ to / in the script.


# How to CSV->AppComponent

- Set up a String with the path `src/ExcelToAppComponent/YOUR_CSVS_PATH`
- Use the static class `CSVToAppComponent`in initilializers as follows

`
String dir = "src/ExcelToAppComponent/YOUR_CSVS_PATH";
List<AppSender> appSenders = CSVToAppComponent.CSVs_to_AppSenders(dir); \\ or
List<AppReceiver> appReceivers = CSVToAppComponent.CSVs_to_AppReceivers(dir);`

- Then continue the initialization.