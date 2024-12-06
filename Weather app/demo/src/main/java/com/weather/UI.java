package com.weather;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;


public class UI extends JFrame {
    private JPanel mainPanel;
    private Font font;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private final int[] tabCount = {0}; 
    private static String inPassDB;
    private static String inUserName = "root";
    private static String inDBName;
    private DataBase weatherDB;
    private final HashSet<String> existingLocations = new HashSet<>();
    private HashMap <String,Weather> weatherInWeek;
    
    public UI() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1130, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("Weather app/demo/src/main/resources/weather-app.png").getImage());
        loadCustomFont("Weather app/demo/src/main/resources/font.ttf");
        setInDBName("my_database");
        setInUsrName("root");
        setInPassDB("7294567");
        activateDB();
        initLayout();
    };

    public DataBase getDB() {
        return this.weatherDB;
    };

    public void activateDB() {
        this.weatherDB = new DataBase(this.getInPassDB(), this.getInUsrName(), this.getInDBName());
        return;
    };

    public void setInPassDB(String inputPass) {
        this.inPassDB = inputPass;
        return;
    };

    private String getInPassDB() {
        return this.inPassDB;
    };

    private String getInUsrName() {
        return this.inUserName;
    };

    public void setInUsrName(String inUsrName) {
        inUserName = inUsrName;
        return;
    };

    private String getInDBName() {
        return inDBName;  
    };

    public void setInDBName(String inDBName) {
       this.inDBName = inDBName;  
       return;
    };

    private void initLayout() {
        mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, 1130, 700);
        add(mainPanel);

    
        JPanel homePanel = createPanel("#abd8ff");
        homePanel.setBounds(430, 0, 700, 1090);
        homePanel.setOpaque(true); 
        homePanel.setPreferredSize(homePanel.getBounds().getSize());

        JPanel hidePanel = createPanel("#abd8ff");
        hidePanel.setBounds(430, 0, 700, 700);
        hidePanel.setOpaque(true); 
        mainPanel.add(hidePanel);


        JScrollPane scrollHomePane = new JScrollPane(homePanel);
        scrollHomePane.setBounds(430, 0, 700, 700);
        scrollHomePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollHomePane.getVerticalScrollBar().setUnitIncrement(50);
        mainPanel.add(scrollHomePane, "Home");
    
        JPanel searchPanel = createPanel("#cfe8ff");
        searchPanel.setBounds(0, 0, 430, 700);
        searchPanel.setOpaque(true);  
        mainPanel.add(searchPanel, "Menu");

        JPanel buttonPanel = createPanel("#cfe8ff");
        buttonPanel.setBounds(10,90,410,570);
        buttonPanel.setLayout(new GridLayout(5, 1, 0, 10));
        mainPanel.add(buttonPanel);

        addComponents(homePanel,searchPanel,buttonPanel,hidePanel);
    }

    private JPanel createPanel(String colorHex) {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.decode(colorHex));
        return panel;
    }

    private void addComponents(JPanel homePanel,JPanel searchPanel,JPanel buttonPanel,JPanel hidePanel) {

         String paragraph = "Welcome to the weather app\n\n"+
         "To start, Enter the location in the obvious text box and press either Enter or the Magnifying glass icon\n\n"+
         "Hide this window by clicking the button that will be created shortly once you pressed Enter\n\n"+
         "To switch between Faraheit and celcius click the Temperature";
        JLabel helpLabel = createLabel("<html>" + paragraph.replaceAll("\n", "<br>") + "</html>",100   ,0,500,700,24,SwingConstants.CENTER);
        hidePanel.add(helpLabel);
        hidePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //this is to block clicking on the component in the home panel
            }
        });

        JLabel weatherConditionImage = createImageLabel("Weather app/demo/src/main/resources/cloudy.png", 200, 200, 30,30);
        homePanel.add(weatherConditionImage);

        JToggleButton tempToggle = createTemperatureToggle(220,50);
        homePanel.add(tempToggle);

        JLabel weatherConditionLabel = createLabel("Cloudy", 250, 70, 400, 36, 32, SwingConstants.LEFT);
        homePanel.add(weatherConditionLabel);

        JLabel humidityImage = createImageLabel("Weather app/demo/src/main/resources/humidity.png", 74, 66, 180, 250);
        JLabel humidLabel = createLabel("Humidity", 100, 250, 100, 55, 16, SwingConstants.LEFT);
        JLabel humidityLabel = createLabel("80%", 100, 270, 100, 55, 16, SwingConstants.LEFT);
        homePanel.add(humidityImage);
        homePanel.add(humidLabel);
        homePanel.add(humidityLabel);

        JLabel windspeedImage = createImageLabel("Weather app/demo/src/main/resources/windspeed.png", 74, 66, 410, 250);
        JLabel windLabel = createLabel("Windspeed", 300, 250, 100, 55, 16, SwingConstants.LEFT);
        JLabel windspeedLabel = createLabel("15 Km/h", 300, 270, 100, 55, 16, SwingConstants.LEFT);
        homePanel.add(windspeedImage);
        homePanel.add(windLabel); 
        homePanel.add(windspeedLabel);   

        JLabel timeLabel = createLabel("10/10/2020", 500, 160, 150, 36, 24, SwingConstants.RIGHT);
        homePanel.add(timeLabel);

        JLabel placeLabel = createLabel("N/A", 550, 100, 100, 36, 24, SwingConstants.RIGHT);
        homePanel.add(placeLabel);

        JTable forecastTable=addForecastTable(100,420);
        homePanel.add(forecastTable);
    
        JTable hourlyTable=addForecastTable(100,420);
        homePanel.add(hourlyTable);
        
        JButton tableLabel = createB("Hourly data", 100,380,200,36,24,SwingConstants.LEFT,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
            hourlyTable.setVisible(true);
            forecastTable.setVisible(false);
            }
        });
        homePanel.add(tableLabel);

        JButton tableLabel_2 = createB("Daily data + Forcast", 350,380,300,36,24,SwingConstants.LEFT,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                hourlyTable.setVisible(false);
                forecastTable.setVisible(true);
            }
        });
        homePanel.add(tableLabel_2);


        JTextField searchField = new JTextField(20){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.setFont(font);
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = 5;
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("Enter location", textX, textY);
                    g2.dispose();
                }
            }
        };


        searchField.setBounds(40,20,300,50);
        searchField.setFont(font);
        searchPanel.add(searchField);
        
        
        JButton searchButton = new JButton(loadImage("Weather app/demo/src/main/resources/search.png", 50, 50));
        searchButton.setFocusPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setBounds(350,20,50,50);
        if (!searchField.getText().isEmpty() && !searchField.getText().equals("Enter location...")) {
            
        }
        
        //main logic
        ActionListener e=new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tabCount[0] >= 5) {
                    JOptionPane.showMessageDialog(mainPanel, "You can add no more than five location");
                    return;
                }

                String searchText = searchField.getText();

                if (!searchText.isEmpty() && !searchText.equals("Enter location...")) {

                    if (existingLocations.contains(searchText)) {
                        return;
                    }
                    existingLocations.add(searchText);

                    weatherInWeek=loadData(searchText);

                    Weather weather=weatherInWeek.get(LocalDate.now().toString());

                    final HashMap<String,Weather> local=weatherInWeek;

                    JPanel resultButton = createButton(searchText, 0, 0, weather,new MouseAdapter() {
                        
                    

                        @Override
                        public void mouseClicked(MouseEvent e) {

                            //main(home) panel update
                            hidePanel.setVisible(true);
                            weatherConditionImage.setIcon(getWeatherIcon(weather.getStatus(), 200, 200));
                            tempToggle.setText(weather.getMeanCelTemp().toString()+"°C");
                            weatherConditionLabel.setText(weather.getStatus());
                            humidityLabel.setText(weather.getPrecipitation().toString());
                            windspeedLabel.setText(weather.getWindSpeed().getSpeed().toString()+" Km/h");
                            placeLabel.setText(searchText);
                            timeLabel.setText(LocalDate.now().toString());
                            
                            //daily table update
                            int i=0;
                            for (String date : local.keySet()) {
                                Weather weather_=local.get(date);
                                forecastTable.setValueAt(getDayOfWeek(date), i, 0);
                                forecastTable.setValueAt(weather_.getStatus(),i,1);
                                forecastTable.setValueAt(getWeatherIcon(weather_.getStatus(),15,15),i,2);
                                forecastTable.setValueAt(weather_.getMeanCelTemp().toString()+"°C",i,3);  
                                forecastTable.setValueAt(df.format(weather_.getPrecipitation()),i,4);
                                forecastTable.setValueAt(weather_.getWindSpeed().getSpeed().toString()+" Km/h", i, 5);
                                i++;
                            }

                            //hourly weather update
                            HashMap <String,Weather> weatherInHours= weather.getWeatherInHours();
                            i=0;
                            for (String date : weatherInHours.keySet()) {
                                Weather weather__=weatherInHours.get(date);
                                hourlyTable.setValueAt(date, i, 0);
                                hourlyTable.setValueAt(weather__.getStatus(),i,1);
                                hourlyTable.setValueAt(getWeatherIcon(weather__.getStatus(),15,15),i,2);
                                hourlyTable.setValueAt(weather__.getMeanCelTemp().toString()+"°C",i,3);  
                                hourlyTable.setValueAt(df.format(weather__.getPrecipitation()),i,4);
                                hourlyTable.setValueAt(weather__.getWindSpeed().getSpeed().toString()+" Km/h", i, 5);
                                hourlyTable.setVisible(false);
                                i++;
                            }
                            hidePanel.setVisible(false);
                            homePanel.repaint();
                        }
                    });
                    buttonPanel.add(resultButton);
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                    
                    tabCount[0]++;
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Please enter a valid location to search");
                }
            }
        };
        searchButton.addActionListener(e);
        searchField.addActionListener(e);
        searchPanel.add(searchButton);
    }

    private static HashMap<String,Weather> loadData(String searchText){
        Location location=new Location(searchText,inPassDB,inUserName,inDBName);
        return location.getWeatherInWeek();
    }

    private JPanel createButton(String text, int x, int y, Weather weather,MouseAdapter action) {
        JPanel button = new JPanel();
        button.setLayout(new BorderLayout());
        button.setLocation(x, y);
        button.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        button.setOpaque(false);

        button.addMouseListener(action);
    
        JButton deleteButton = new JButton("x");
        deleteButton.setFont(font.deriveFont(20f));      
        deleteButton.setFocusPainted(false);                     
        deleteButton.setBorderPainted(false);  
        deleteButton.setOpaque(false);
        deleteButton.setContentAreaFilled(false);                               
        
    
        deleteButton.addActionListener(e -> {
            Container parent = button.getParent();
            if (parent != null) {
                parent.remove(button); 
                parent.revalidate();  
                parent.repaint();     
                tabCount[0]--;
                existingLocations.remove(text);
            }
        });
        System.out.println(weather);    
        JLabel locationLabel = new JLabel(text, SwingConstants.LEFT);
        locationLabel.setFont(font.deriveFont(24f));

        JLabel weatherLabel = new JLabel(weather.getMeanCelTemp().toString()+" "+weather.getStatus(), SwingConstants.LEFT);
        
        weatherLabel.setFont(font.deriveFont(24f));
        
        JLabel weatherIcon = new JLabel(getWeatherIcon(weather.getStatus(), 50, 50));
    

        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(deleteButton, BorderLayout.EAST);
        topPanel.add(locationLabel, BorderLayout.CENTER);
    
        button.add(topPanel, BorderLayout.NORTH);
        button.add(weatherLabel, BorderLayout.CENTER);
        button.add(weatherIcon, BorderLayout.EAST);
    
        return button;
    }

    private JTable addForecastTable(int x,int y) {
    
        String[] columnNames = {"", "", "", "", "", ""};
        Object[][] data = {
            {"Today", "Sunny", loadImage("Weather app/demo/src/main/resources/sunny.png", 15, 15), "28°C", "50%", "10 mph"},
            {"Wed", "Sunny", loadImage("Weather app/demo/src/main/resources/sunny.png", 15, 15), "26°C", "45%", "12 mph"},
            {"Thu", "Cloudy", loadImage("Weather app/demo/src/main/resources/cloudy.png", 15, 15), "31°C", "60%", "14 mph"},
            {"Fri", "Sunny", loadImage("Weather app/demo/src/main/resources/sunny.png", 15, 15), "36°C", "40%", "9 mph"},
            {"Sat", "Cloudy", loadImage("Weather app/demo/src/main/resources/cloudy.png", 15, 15), "31°C", "55%", "13 mph"},
            {"Sun", "Snow", loadImage("Weather app/demo/src/main/resources/snow.png", 15, 15), "11°C", "85%", "20 mph"},
            {"Mon", "Sunny", loadImage("Weather app/demo/src/main/resources/sunny.png", 15, 15), "12°C", "50%", "10 mph"},
            {"Tue", "Sunny", loadImage("Weather app/demo/src/main/resources/sunny.png", 15, 15), "13°C", "50%", "10 mph"},
            {"Wed", "Sunny", loadImage("Weather app/demo/src/main/resources/sunny.png", 15, 15), "14°C", "50%", "10 mph"}
            
        };
    
        JTable forecastTable = new JTable(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 2) ? ImageIcon.class : String.class;
            }
    
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        forecastTable.setShowVerticalLines(false);
        forecastTable.setOpaque(false);
        forecastTable.setFont(font.deriveFont(16f));
        forecastTable.setRowHeight(70);

        ((DefaultTableCellRenderer)forecastTable.getDefaultRenderer(Object.class)).setOpaque(false);
        ((DefaultTableCellRenderer)forecastTable.getDefaultRenderer(ImageIcon.class)).setOpaque(false);
        forecastTable.setFocusable(false);
        
        
    
        // Adjust column widths for a better fit
        forecastTable.getColumnModel().getColumn(0).setPreferredWidth(90);  // Day
        forecastTable.getColumnModel().getColumn(1).setPreferredWidth(90);  // Weather
        forecastTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Icon
        forecastTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Temp (Low/High)
        forecastTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Humidity
        forecastTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Windspeed
    
        forecastTable.setFont(font.deriveFont(16f));
        forecastTable.setRowHeight(70);
    
        forecastTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = forecastTable.columnAtPoint(e.getPoint());
        
                // Check if the temperature column (column 3) was clicked
                if (col == 3) {
                    for (int row = 0; row < forecastTable.getRowCount(); row++) {
                        String tempValue = (String) forecastTable.getValueAt(row, col);
        
                        String newTempValue="";
                        if (tempValue.endsWith("°C")) {
                            
                            float tempC = Float.parseFloat(tempValue.replace("°C", "").trim());
            
                            float tempF = (float) (tempC * 9.0 / 5.0 + 32);
                            newTempValue= df.format(tempF) + "°F";  
                        } 
                        
                        else if (tempValue.endsWith("°F")) {
                            
                            float tempF = Float.parseFloat(tempValue.replace("°F", "").trim());
                            
                            
                            float tempC = (float) ((tempF - 32) * 5.0 / 9.0);
                            newTempValue= df.format(tempC) + "°C";  
                        }
                        forecastTable.setValueAt(newTempValue, row, col);
                    }
                }
            }
        });

        forecastTable.setBounds(x,y, 500, 640);
        return forecastTable;
    }

    private JLabel createImageLabel(String resourcePath, int width, int height, int x, int y) {
        JLabel label = new JLabel(loadImage(resourcePath, width, height));
        label.setBounds(x, y, width, height);
        return label;
    }

    private JLabel createLabel(String text, int x, int y, int width, int height, int fontSize, int alignment) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font.deriveFont((float)fontSize)); 
        label.setHorizontalAlignment(alignment);
        return label;
    }

    private JButton createB(String text, int x, int y, int width, int height, int fontSize, int alignment,ActionListener action) {
        JButton label = new JButton(text);
        label.setBounds(x, y, width, height);
        label.setFocusPainted(false);
        label.setContentAreaFilled(false);
        label.setBorderPainted(false);
        label.setFont(font.deriveFont((float)fontSize)); 
        label.setHorizontalAlignment(alignment);
        label.addActionListener(action);
        return label;
    }

    private JToggleButton createTemperatureToggle(int x,int y) {
        
        JToggleButton tempToggle = new JToggleButton("25.0°C");
        tempToggle.setFont(font.deriveFont(48f));
        tempToggle.setAlignmentY(SwingConstants.LEFT);
        tempToggle.setBounds(x, y, 200, 200);
        tempToggle.setOpaque(false);
        tempToggle.setFocusPainted(false);
        tempToggle.setContentAreaFilled(false);
        tempToggle.setFocusable(false);
        tempToggle.setBorderPainted(false);
        tempToggle.addActionListener(e -> {
            String newTempValue;
            if (tempToggle.getText().endsWith("C")) {
                // Convert to Fahrenheit
                double tempC = Double.parseDouble(tempToggle.getText().replace("°C", "").trim());
                double tempF = tempC * 9.0 / 5.0 + 32;
                newTempValue = df.format(tempF) + "°F";
            } else {
                // Convert to Celsius
                double tempF = Double.parseDouble(tempToggle.getText().replace("°F", "").trim());
                double tempC = (tempF - 32) * 5.0 / 9.0;
                newTempValue = df.format(tempC) + "°C";
            }
            
            tempToggle.setText(newTempValue);
        }); 
        return tempToggle;
    }

    private ImageIcon loadImage(String resourcePath, int width, int height) {
        try {
            BufferedImage image;
            if (resourcePath.startsWith("http")) {
                image = ImageIO.read(new URL(resourcePath));
            } else {
                image = ImageIO.read(new File(resourcePath));
            }
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Could not find resource: " + resourcePath);
            return null;
        }
    }
    
    public ImageIcon getWeatherIcon(String condition,int height,int width) {
        if (condition.contains("Sun")) {
            return loadImage("Weather app/demo/src/main/resources/sunny.png", width, height);
        } else if (condition.contains("Rain")||condition.contains("rain")) {
            return loadImage("Weather app/demo/src/main/resources/rain.png", width, height);
        } else if (condition.contains("Fog")||condition.contains("Mist")) {
            return loadImage("Weather app/demo/src/main/resources/fog.png", width, height);
        } else if (condition.contains("Drizzle")||condition.contains("drizzle")) {
            return loadImage("Weather app/demo/src/main/resources/drizzle.png", width, height);
        } else if (condition.contains("Snow")||condition.contains("snow")) {
            return loadImage("Weather app/demo/src/main/resources/snow.png", width, height);
        } else {
           return loadImage("Weather app/demo/src/main/resources/cloudy.png", width, height);
        }
}

    private void loadCustomFont(String fontPath) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(24f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("Could not load font: " + fontPath);
            font = new Font("Dialog", Font.PLAIN, 24);
        }
    }

    public String getDayOfWeek(String date){
        DayOfWeek dayOfWeek=LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd")).getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY -> {
                return "Mon";
            }
            case TUESDAY -> {
                return "Tue";
            }
            case WEDNESDAY -> {
                return "Wed";
            }
            case THURSDAY -> {
                return "Thur";
            }
            case FRIDAY -> {
                return "Fri";
            }
            case SATURDAY -> {
                return "Sat";
            }
            case SUNDAY -> {
                return "Sun";
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.setVisible(true);
        });
    }
}
