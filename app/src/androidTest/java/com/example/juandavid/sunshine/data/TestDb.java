/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.juandavid.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;


public class TestDb extends AndroidTestCase {

        public static final String LOG_TAG = TestDb.class.getSimpleName();

        // Since we want each test to start with a clean slate
        void deleteTheDatabase() {

                mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        }

        /*
            This function gets called before each test is executed to delete the database.  This makes
            sure that we always have a clean test.
         */
        public void setUp() {

                deleteTheDatabase();
        }

        public void testLocationTable(){insertLocation();}

        // First step: Get reference to writable databas

// First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        public void testWeatherTable(){  deleteTheDatabase();

                long locationRowId = insertLocation();

                assertFalse("Error: Location Not Inserted Correctly",locationRowId==-1L);

                // Instead of rewriting all of the code we've already written in testLocationTable
                // we can move this code to insertLocation and then call insertLocation from both
                // tests. Why move it? We need the code to return the ID of the inserted location
                // and our testLocationTable can only return void because it's a test.

                // First step: Get reference to writable database
                WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Create ContentValues of what you want to insert
                // (you can use the createWeatherValues TestUtilities function if you wish)
                ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
                // Insert ContentValues into database and get a row ID back

                long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);

                assertTrue(weatherRowId!=-1);

                // Query the database and receive a Cursor back
                Cursor weatherCursor = db.query(
                        WeatherContract.WeatherEntry.TABLE_NAME,  // Table to Query
                        null, // leaving "columns" null just returns all the columns.
                        null, // cols for "where" clause
                        null, // values for "where" clause
                        null, // columns to group by
                        null, // columns to filter by row groups
                        null  // sort order
                );

                // Move the cursor to a valid database row
                assertTrue("Error: No records returned from location query",weatherCursor.moveToFirst()

                ); // No se econtro nada
                // Validate data in resulting Cursor with the original ContentValues
                // (you can use the validateCurrentRecord function in TestUtilities to validate the
                // query if you like)
                TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                        weatherCursor,weatherValues);

                assertFalse("Error: More than one record returned from weather query",
                        weatherCursor.moveToNext());
                // Finally, close the cursor and database
                weatherCursor.close();
                dbHelper.close();}





        public long insertLocation(){
                assertTrue(0==0);

                WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Second Step: Create ContentValues of what you want to insert
                // (you can use the createNorthPoleLocationValues if you wish)
                ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

                // Third Step: Insert ContentValues into database and get a row ID back
                long locationRowId;
                locationRowId=db.insert(WeatherContract.LocationEntry.TABLE_NAME,null,testValues);

                // Verify we got a row back.
                assertTrue(locationRowId!=-1);

                // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
                // the round trip.

                // Fourth Step: Query the database and receive a Cursor back
                // A cursor is your primary interface to the query results.
                Cursor cursor = db.query(
                        WeatherContract.LocationEntry.TABLE_NAME,  // Table to Query
                        null, // all columns
                        null, // Columns for the "where" clause
                        null, // Values for the "where" clause
                        null, // columns to group by
                        null, // columns to filter by row groups
                        null // sort order
                );

                // Move the cursor to a valid database row and check to see if we got any records back
                // from the query
                assertTrue("Error: No Records returned from location query",cursor.moveToFirst()

                );

                // Fifth Step: Validate data in resulting Cursor with the original ContentValues
                // (you can use the validateCurrentRecord function in TestUtilities to validate the
                // query if you like)
                TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                        cursor,testValues);

                // Move the cursor to demonstrate that there is only one record in the database
                assertFalse("Error: More than one record returned from location query",
                        cursor.moveToNext()

                );

                // Sixth Step: Close Cursor and Database
                cursor.close();
                db.close();

                return locationRowId;
        }

}

