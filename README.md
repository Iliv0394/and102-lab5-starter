# Campground Explorer Pt. 2

Submitted by: Isaac Livingston

Campground Explorer Pt. 2 is an Android app that displays campground information from the National Park Service API. This version adds offline support by saving campground data into a local Room database so the user can still view campgrounds without internet access.

Time spent: about 4 hours spent in total

## Required Features

The following required functionality is completed:

- [x] Most recently fetched campground data is stored locally
- [x] Room database is used to save campground information
- [x] Created a CampgroundEntity
- [x] Created a CampgroundDao
- [x] Created an AppDatabase
- [x] Created a CampgroundApplication class
- [x] API data is saved into the local database
- [x] Database operations run using Kotlin coroutines
- [x] Campground data is loaded from the database into the RecyclerView
- [x] Campgrounds still display when Airplane Mode is turned on
- [x] App still displays saved campground data after closing and reopening offline

## Optional Features

The following optional/stretch features are implemented:

- [ ] Swipe to Refresh
- [ ] Search campground data
- [ ] Offline status message
- [ ] Cache preference setting

## Additional Features

The following additional features are implemented:

- [x] Campground images are displayed
- [x] Campground name is displayed
- [x] Campground description is displayed
- [x] Campground latitude and longitude are displayed
- [x] Uses the National Park Service API
- [x] Uses Room as the local database
- [x] Uses Kotlin Flow to observe database changes
- [x] Uses Dispatchers.IO for database operations

## Video Walkthrough

Here's a walkthrough of the implemented features:

[ https://drive.google.com/file/d/17XfZ71f_kATGyb6BYnQWJxEagGXg5aSJ/view?usp=sharing ]

## Notes

One challenge was setting up the Room database and connecting it to the existing campground app.

Another challenge was making sure the app loaded campground data from the database instead of only depending on the API.

The app was tested with Airplane Mode turned on. The campground list still displayed after the app was closed and reopened, showing that the offline caching worked correctly.

## License

Copyright 2026 Isaac Livingston

Licensed under the Apache License, Version 2.0.
