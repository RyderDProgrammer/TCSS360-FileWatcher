TCSS 360 File Watcher Version 1.0
Developers: Manjinder Ghuman, Ryder Deback, Brendan Tucker

--MAIN WINDOW--
    -File Menu-
        File:
            Start: Starts the program IF you have a directory selected.
            Stop: Will stop the program along with the timer.
            Query Database: If database is connected will alow
            Close: Will close the program, another way other than using the X.
        Debug (USER SHOULD NOT SEE THIS!):
            Add 10 events: Adds 10 test events into the table for testing purposes.
            Add 100 events: Adds 100 test events into the table for testing purposes.
            Add one of each file type: Adds one of each extension type available, and one extra, for testing purposes.
        Database:
            Connect to Database: Connects to the database and enables query,write to database, and disconnect database
            Disconnect Database: Disconnects the database and disabled the query and write to database buttons.
        Email:
            Send File via Email: Asks the user for an email to send the selected file to.
        About:
            About: Displays information about the developers and the versioning.

    -Picture Buttons-
        Start: Alternate way to start the program IF a directory is selected.
        Stop: Alternate way to stop the program.
        Database: Writes the current table to the database, if the database is not connected will ask user if they want to.
        Clear: Clears out the current table and resets the program to starting position, does not erase from the database.

    -Main Display-
        Monitor by extension: The extension that the user wishes to watch for, all of them, a specified one, or their own entry
        Directory to monitor: The directory that is being monitored, browse will fill this.
        Browse: Opens the users local machine and allows them to select a directory.
        Start: Starts the program if a proper directory is selected.
        Stop: Stops the program and the timer.
        File Table: Displays all that are being monitored.
        Write to database: IF there is a connection to the database, it will write the events into the local database.
        Query: Opens the query window.
        Reset: Resets the table and window to its starting point.

--QUERY WINDOW--
(Only available if there is a database connected)
    -Query to Select-
        -Manual Query-
            Choose file detail: Default option to display what the user is searching for manually.
            File Name: Allows for a fuzzy search for file names.
            File Extension: Allows the user to select multiple extension types to view data on them.
            Path to File Location: Opens a directory browser for the user to specify a pathway.
            Type of Activity: The type of activity (CREATED, DELETED, MODIFIED) that the user is searching for.
            Between Two Dates: Allows the user to enter in a start and end date to search for entries.
        Query 1 (All events from today): Will display all the entries in the database registered by todays local date of the users machine.
        Query 2 (Top 5 frequently modified file types): Will display the top 5 frequently modified file types and all their entries
        Query 3 (Most Common Events for Each extension): Will display the most common event (CREATED, DELETED, MODIFIED), how many times its occured and for what extension.
    Export to CSV: Opens a directory for the user to store all the entries that appear in the database query window as a CSV.
    Return to Main Window: Returns to the main visual window, similar to the X in the top right.
    Reset Database: Hard resets the database and removes ALL information inside of it.