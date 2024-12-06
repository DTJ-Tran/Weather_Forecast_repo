package com.weather;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataBase {
    // A client object will retrieve and get the data for the front-end and feed to back-end
    // The database in here will be a weather Forecast data that our team has been modified for specific use-case

    private String passWord = ""; // Denote the dataBase Pass
    private String userName = ""; // Denote the userName of dataBase
    private String dataBase = ""; // the name of the Database you want retrieve the data
    private Connection conn;
    
    /*
     *
     * This class is for working with the database and retrieve the prediction result from the back-end
     * With database -> get the data of 7 day weather (include the date, the wind_speed, mean_temp and precipitation)
     * With database -> get the data of 1 day (with specific feature - the temperature)
     * With database -> retrieve the data of a feature from 7 day (wind_speed, mean_temp, precipitation)
     * Feed into the prediction (the data of 7 days on 1 features)
     */

    @SuppressWarnings("UnnecessaryReturnStatement")
    public void setPassWord (String inputPass) {
        this.passWord = inputPass;
        return;
    };

    @SuppressWarnings("UnnecessaryReturnStatement")
    private String getPassWord () {
        return this.passWord;
    };
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    private String getUsrName () {
        return this.userName;
    };

    @SuppressWarnings("UnnecessaryReturnStatement")
    private String getDBName () {
        return this.dataBase;
    };

    @SuppressWarnings("UnnecessaryReturnStatement")
    public void setUserName (String inputUserName) {
        this.userName = inputUserName;
        return;
    };

    @SuppressWarnings("UnnecessaryReturnStatement")
    public void setDatabase (String inputDBName) {
        this.dataBase = inputDBName;
        return;
    };

    public DataBase() {
        this.passWord = "";
        this.passWord = "";
        this.dataBase = "";
        this.conn = null;
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public DataBase(String inputPass, String inputUserName, String inputDBName) {
        this.setPassWord(inputPass);
        this.setUserName(inputUserName);
        this.setDatabase(inputDBName);
        try {
            this.conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/" + this.dataBase, this.userName, this.passWord);
        } catch (SQLException e) {
            System.err.println("An error has occur please recheck !");
            e.printStackTrace();
        }
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public void activateConnection() {
        if (this.conn != null) {
            System.out.println("Already has the connection - beware this will dirrect you to difference database");
        } else {
            System.out.println("Start establish your connection !");
        }
        try {
            this.conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/" + this.getDBName(), this.getUsrName(), this.getPassWord());
        } catch (SQLException e) {
            System.err.println("An error has occur please recheck !");
            e.printStackTrace();
        }
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public <Type> List<Type> getDataSQLObjectArr(String tableName, String colName , String orderKey, Class<Type> type, String limit) { 
        // use for retrieve any type of data - no limmited
        List<Type> result = new ArrayList<>();
        
        try (java.sql.Statement stmt = this.conn.createStatement()) {
            String query;
            if (!limit.isEmpty()) {
                query = "SELECT * FROM " + tableName + " ORDER BY " + orderKey + " DESC LIMIT " + limit;
            }
            else {
                query = "SELECT * FROM " + tableName + " ORDER BY " + orderKey + " DESC LIMIT 7";
            };
            // example: query = "SELECT * FROM weather_forecast ORDER BY date DESC LIMIT 7";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                    if (type == Float.class) {
                        result.add(type.cast(rs.getFloat(colName)));
                    } else if (type == Integer.class) {
                        result.add(type.cast(rs.getInt(colName)));
                    } else if (type == String.class) {
                        result.add(type.cast(rs.getString(colName)));
                    } else if (type == Double.class) {
                        result.add(type.cast(rs.getDouble(colName)));
                    } else {
                        throw new IllegalArgumentException("Unsupported type: " + type);
                    }
                }
            
            }
        catch (SQLException e) {
            System.out.println("Something wrong in here check the error message for more !");
            e.printStackTrace();
        }
        return result;
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Float> getDataSQLFloatArr(String tableName, String colName , String orderKey) { 
        // use for retrieve an array of Float - in here to get the 7 elements (System Design)
        // order from final field to first field
        // Note the column you want to retrieve data must has the type Float
        
        List<Float> result = new ArrayList<>();
        try ( java.sql.Statement stmt = this.conn.createStatement()) {
            String query = "SELECT * FROM " + tableName + " ORDER BY " + orderKey + " DESC LIMIT 7";
            // example: query = "SELECT * FROM weather_forecast ORDER BY date DESC LIMIT 7";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                
                result.add(rs.getFloat(colName));
            
            }
        } catch (SQLException e) {
            System.out.println("Something wrong in here check the error message for more !");
            e.printStackTrace();
        }
        return result;
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public List<String> getDataSQLStringArr(String tableName, String colName, String orderKey) {
        // Return a set of string value of 7 elements
        List<String> result = new ArrayList<>();
        try ( java.sql.Statement stmt = this.conn.createStatement()) {
            String query = "SELECT * FROM " + tableName + " ORDER BY " + orderKey + " DESC LIMIT 7";
            // example: query = "SELECT * FROM weather_forecast ORDER BY date DESC LIMIT 7";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                
                result.add(rs.getString(colName));
            
            }
        } catch (SQLException e) {
            System.out.println("Something wrong in here check the error message for more !");
            e.printStackTrace();
        }
        return result;
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Integer> getDataSQLIntArr(String tableName, String colName, String orderKey) {
        // Return a set of 7 integers
        List<Integer> result = new ArrayList<>();
        try ( java.sql.Statement stmt = this.conn.createStatement()) {
            String query = "SELECT * FROM " + tableName + " ORDER BY " + orderKey + " DESC LIMIT 7";
            // example: query = "SELECT * FROM weather_forecast ORDER BY date DESC LIMIT 7";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                
                result.add(rs.getInt(colName));
            
            }
        } catch (SQLException e) {
            System.out.println("Something wrong in here check the error message for more !");
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public Map<String, Object> getDataAtCertainId(String tableName, Integer id) {
        Map<String, Object> specificEl = new HashMap<>();
        try ( java.sql.Statement stmt = this.conn.createStatement()) {
            String query = "SELECT * FROM " + tableName + " WHERE id = " + id.toString();
            // example: query = SELECT * FROM weather_forecast WHERE id = 145;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                specificEl.put("id", rs.getInt("id"));
                specificEl.put("date",rs.getString("date"));
                specificEl.put("precipitation",rs.getFloat("precipitation"));
                specificEl.put("wind_speed",rs.getFloat("wind_speed"));
                specificEl.put("mean_temp", rs.getFloat("mean_temp"));
                specificEl.put("min_temp", rs.getFloat("min_temp"));
                specificEl.put("max_temp", rs.getFloat("max_temp"));
                specificEl.put("weather_status", rs.getString("weather_status")); // this will be convert by external Class
                
            }
        } catch (SQLException e) {
            System.out.println("Something wrong in here check the error message for more !");
            e.printStackTrace();
        }
        return specificEl;
    };

    @SuppressWarnings("CallToPrintStackTrace")
    public Map<String, Object> getRowWithCondition(String tableName, String condition) {
        Map<String, Object> specificEl = new HashMap<>();
        try ( java.sql.Statement stmt = this.conn.createStatement()) {
            String query = "SELECT * FROM " + tableName + " " + condition;
            // example: query = SELECT * FROM weather_forecast WHERE condition;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                specificEl.put("id", rs.getInt("id"));
                specificEl.put("date",rs.getString("date"));
                specificEl.put("precipitation",rs.getFloat("precipitation"));
                specificEl.put("wind_speed",rs.getFloat("wind_speed"));
                specificEl.put("mean_temp", rs.getFloat("mean_temp"));
                specificEl.put("min_temp", rs.getFloat("min_temp"));
                specificEl.put("max_temp", rs.getFloat("max_temp"));
                specificEl.put("weather_status", rs.getString("weather_status")); // this will be convert by external Class
                
            }
        } catch (SQLException e) {
            System.out.println("Something wrong in here check the error message for more !");
            e.printStackTrace();
        }
        return specificEl;
    };
 
    @SuppressWarnings("UnnecessaryReturnStatement")
    public void addRowDate(String tableName, String dateVal, String weatherStatus ,List<Double> args, List<String> colName) {
        /*
         * Args format: [precipitation, wind_speed, weather_status, mean_temp, max_temp, min_temp]
         * colName format: [id, date, precipitation, wind_speed, weather_status, mean_temp, max_temp, min_temp]
         */
        if (colName.isEmpty() || args.isEmpty()) {
            System.out.println("Check the args list or the list of column names - one of them is empty.");
            return;
        }
        // System.out.println("args size: " + args.size()); 
        if (colName.size() < 8 || args.size() < 5) {
            
            System.out.println("Not enough columns or parameters for the query. Check the column names and arguments again.");
            return;
        }
    
        // Assigning column names based on the list
        String idCol = colName.get(0);           // id
        String dateCol = colName.get(1);         // date
        String precipitationCol = colName.get(2); // precipitation
        String windSpeedCol = colName.get(3);    // wind_speed
        String weatherStatusCol = colName.get(4); // weather_status
        String meanTempCol = colName.get(5);     // mean_temp
        String maxTempCol = colName.get(6);      // max_temp
        String minTempCol = colName.get(7);      // min_temp
    
    
        // Assigning argument values based on the list
        String precipitation = args.get(0).toString(); // precipitation value
        String windSpeed = args.get(1).toString();     // wind_speed value
        String meanTemp = args.get(2).toString();      // mean_temp value
        String maxTemp = args.get(3).toString();       // max_temp value
        String minTemp = args.get(4).toString();       // min_temp value
    
        // Constructing the SQL query
        String query = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) " +
            "SELECT MAX(%s) + 1, " + // id
            "'%s', " + // date
            "%s, %s, '%s', %s, %s, %s " + // precipitation, wind_speed, weather_status, mean_temp, max_temp, min_temp
            "FROM %s",
            tableName,
            idCol, dateCol, precipitationCol, windSpeedCol, weatherStatusCol, meanTempCol, maxTempCol, minTempCol, // Columns to insert
            idCol, dateVal, // MAX(id) + 1 and DATE_ADD logic
            precipitation, windSpeed, weatherStatus, meanTemp, maxTemp, minTemp, // Values
            tableName // Table for FROM clause
        );
    
        // Executing the query
        try (java.sql.Statement stmt = this.conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error while executing the query. Check the error message below:");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    public void addNewRowDB(String tableName, List<String> args, List<String> colName) {
        /*
         * Args format: [precipitation, wind_speed, weather_status, mean_temp, max_temp, min_temp]
         * colName format: [id, date, precipitation, wind_speed, weather_status, mean_temp, max_temp, min_temp]
         */
        if (colName.isEmpty() || args.isEmpty()) {
            System.out.println("Check the args list or the list of column names - one of them is empty.");
            return;
        }
        if (colName.size() < 8 || args.size() < 6) {
            System.out.println("Not enough columns or parameters for the query. Check the column names and arguments again.");
            return;
        }
    
        // Assigning column names based on the list
        String idCol = colName.get(0);           // id
        String dateCol = colName.get(1);         // date
        String precipitationCol = colName.get(2); // precipitation
        String windSpeedCol = colName.get(3);    // wind_speed
        String weatherStatusCol = colName.get(4); // weather_status
        String meanTempCol = colName.get(5);     // mean_temp
        String maxTempCol = colName.get(6);      // max_temp
        String minTempCol = colName.get(7);      // min_temp
    
        // Assigning argument values based on the list
        String precipitation = args.get(0); // precipitation value
        String windSpeed = args.get(1);     // wind_speed value
        String weatherStatus = args.get(2); // weather_status 
        String meanTemp = args.get(3);      // mean_temp value
        String maxTemp = args.get(4);       // max_temp value
        String minTemp = args.get(5);       // min_temp value
    
        // Constructing the SQL query
        String query = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) " +
            "SELECT MAX(%s) + 1, " + // id
            "DATE_ADD(MAX(%s), INTERVAL 1 DAY), " + // date
            "%s, %s, '%s', %s, %s, %s " + // precipitation, wind_speed, weather_status, mean_temp, max_temp, min_temp
            "FROM %s",
            tableName,
            idCol, dateCol, precipitationCol, windSpeedCol, weatherStatusCol, meanTempCol, maxTempCol, minTempCol, // Columns to insert
            idCol, dateCol, // MAX(id) + 1 and DATE_ADD logic
            precipitation, windSpeed, weatherStatus, meanTemp, maxTemp, minTemp, // Values
            tableName // Table for FROM clause
        );
    
        // Executing the query
        try (java.sql.Statement stmt = this.conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error while executing the query. Check the error message below:");
            e.printStackTrace();
        }
    };
    
    public void deleteARowDB(String tableName, String deleteCondition) {
        try (java.sql.Statement stmt = this.conn.createStatement()) {
            String query = String.format("DELETE FROM %s WHERE %s", tableName, deleteCondition);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " row(s) delete.");
        } catch (SQLException e) {
            System.out.println("Something wrong in the tableName or deleteCondition check the error message for more !");
            e.printStackTrace();
        }
        return;
    };

    public void deleteAllRow(String tableName) {
        try (java.sql.Statement stmt = this.conn.createStatement()) {
            String query = String.format("DELETE FROM %s", tableName);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println(rowsAffected + " row(s) delete.");

            // Reset the AUTO_INCREMENT value
            String resetAutoIncrementQuery = String.format("ALTER TABLE %s AUTO_INCREMENT = 1", tableName);
            stmt.executeUpdate(resetAutoIncrementQuery);
            System.out.println("AUTO_INCREMENT value reset to 1.");
        } catch (SQLException e) {
            System.out.println("Something wrong in the tableName or deleteCondition check the error message for more !");
            e.printStackTrace();
        }
        return;
    };

    public void createTableDB(String tableName) {
        // Validate table name to prevent SQL injection or invalid names
        if (!isValidTableName(tableName)) {
            System.err.println("Invalid table name: " + tableName);
            return;
        }
    
        // SQL to create the table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + tableName + "` ("
            + "`id` INT(11) NOT NULL AUTO_INCREMENT ," // set not auto-increment
            + "`date` DATE NOT NULL, "
            + "`precipitation` DOUBLE, "
            + "`wind_speed` DOUBLE, "
            + "`weather_status` VARCHAR(255), "
            + "`mean_temp` DOUBLE, "
            + "`max_temp` DOUBLE, "
            + "`min_temp` DOUBLE, "
            + "PRIMARY KEY (`id`, `date`))";
    
        try (java.sql.Statement stmt = this.conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table '" + tableName + "' is ready.");
        } catch (SQLException e) {
            System.err.println("Error creating table '" + tableName + "'. Check the following error message:");
            e.printStackTrace();
        }
    }
    
    public boolean doesTableExist(String tableName) {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(query)) {
            pstmt.setString(1, this.getDBName()); // Set the database name
            pstmt.setString(2, tableName);    
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return (rs.getInt(1) > 0); // Table exists if count > 0
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    };

    /**
     * Validate the table name to ensure it contains only alphanumeric characters and underscores.
     * @param tableName The table name to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidTableName(String tableName) {
        // MySQL naming convention: Allows alphanumeric, underscores, and spaces (if enclosed).
        return tableName.matches("[a-zA-Z_][a-zA-Z0-9_ ]{0,63}");
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    public static void main(String[] args_1) {
        // Checking is the object work properly
        DataBase testDataBase = new DataBase("TheGoodPlace", "root", "weatherForecast");
        // String tableName = "weatherForecast";
        // List<String> colName  = new ArrayList<String>();
        // List<Double> args = new ArrayList<Double>();

        // colName.add("id");
        // colName.add("date");
        // colName.add("precipitation");
        // colName.add("wind_speed");
        // colName.add("weather");
        // colName.add("mean_temp");

        // args.add(0.2);
        // args.add(2.3);
        // args.add(1.0);
        // args.add(13.04);
        
        // testDataBase.addNewRowDB(tableName, args, colName);
        // testDataBase.deleteARowDB(tableName, "id = (SELECT max_id FROM (SELECT MAX(id) AS max_id FROM  weather_forecast) AS temp)");
        System.out.println(testDataBase.doesTableExist("Okinawa"));
        return;
};


}