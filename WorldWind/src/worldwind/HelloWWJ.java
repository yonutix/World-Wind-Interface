package worldwind;

import gov.nasa.worldwind.globes.*;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.poi.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.render.markers.*;
import gov.nasa.worldwind.render.Material;
import java.beans.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.measure.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.awt.print.*;

class WWJ extends JFrame {

    Dimension screenSize;
    WorldWindowGLCanvas ww;
    Gazetteer gz;
    Polyline segm;
    RenderableLayer Rlayer;
    Vector<Position> positions = new Vector<Position>();
    MarkerLayer Mlayer = new MarkerLayer();
    Vector<Marker> mkr = new Vector<Marker>();
    MeasureTool measureTool;
    JPanel infoTag;
    Layer politicalBoundries;

    /*
     * Constructor
     */
    WWJ() throws IOException {
        super("OOP Homework");

        //Task 1
        ww = new WorldWindowGLCanvas();
        ww.setModel(new BasicModel());
        Container cp = super.getContentPane();
        //Menu bar
        this.setJMenuBar(setMenuBar());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.screenSize = toolkit.getScreenSize();
        super.setMinimumSize(new Dimension(1280, 720));

        Dimension winSize = new Dimension(super.getWidth(), super.getHeight());
        super.setLocation((this.screenSize.width - winSize.width) / 2,
                (this.screenSize.height - winSize.height) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//close button
        //end Task 1

        //Disable World map layer
        disableLayer("World Map");
        //Set default controls for rotation, zoom elevation etc
        setDefaultControls();

        //initialization and setup for measurements and auxiliar layers
        segm = new Polyline();
        segm.setFollowTerrain(true);
        Rlayer = new RenderableLayer();
        Rlayer.addRenderable(segm);
        ww.getModel().getLayers().add(Rlayer);
        Mlayer.setOverrideMarkerElevation(true);
        Mlayer.setKeepSeparated(false);
        Mlayer.setElevation(1000d);
        ww.getModel().getLayers().add(Mlayer);
        measureTool = new MeasureTool(ww);
        measureTool.setController(new MeasureToolController());
        measureTool.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
        //Search the political boundries layer
        for (Layer layer : ww.getModel().getLayers()) {
            if (layer.toString().compareTo("Political Boundaries") == 0) {
                politicalBoundries = layer;
            }
        }
        //there are used several layers
        JLayeredPane lay = new JLayeredPane();

        addGraphicButtons(lay);


        //Surface measurement panel setup
        JPanel surfacePanel = new JPanel();
        surfacePanel.setVisible(false);
        JTextField surfaceField = new JTextField(14);
        surfaceField.setBorder(null);
        surfaceField.setEditable(false);
        surfacePanel.add(surfaceField);
        lay.add(surfacePanel, new Integer(3));

        //Surface measurement button setup
        JButton ruler = new JButton(new ImageIcon("images/metalruler.jpg"));
        ruler.setBorder(null);
        ruler.addMouseListener(new SurfaceMouseListener(surfacePanel, surfaceField, measureTool, ww, this));
        lay.add(ruler, new Integer(2));


        //Location zoom panel contains a label, a text field, a submit button and a response text field
        //Create all those things and put them together
        JLabel zoomLabel = new JLabel("Insert location and press Find");
        zoomLabel.setForeground(Color.white);
        JTextField zoomLoc = new JTextField();
        zoomLoc.setCaretColor(Color.red);
        zoomLoc.setOpaque(false);
        zoomLoc.setForeground(Color.white);
        JPanel zoomPanel = new JPanel();
        zoomPanel.setBackground(Color.BLACK);
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.PAGE_AXIS));
        JComboBox zoomComboBox = new JComboBox();
        zoomComboBox.addActionListener(new ComboBoxZoomActionListener(this, zoomLoc));
        zoomComboBox.setVisible(false);
        JButton zoomButton = new JButton("Find");
        zoomButton.setForeground(Color.white);
        zoomButton.setBackground(Color.black);
        JTextArea zoomResponse = new JTextArea();
        zoomResponse.setOpaque(false);
        zoomResponse.setForeground(Color.white);
        zoomResponse.setEditable(false);
        zoomButton.addActionListener(new TextButtonActionListener(this, zoomLoc, zoomComboBox, zoomResponse));
        zoomPanel.add(zoomLabel);
        zoomPanel.add(zoomLoc);
        zoomPanel.add(zoomButton);
        zoomPanel.add(zoomComboBox);
        zoomPanel.add(zoomResponse);
        //End Find Location

        JPanel measurePanel = new JPanel();
        measurePanel.setLayout(new GridLayout(5, 1));
        JLabel lb = new JLabel("measure distance");

        JPanel fromPanel = new JPanel();
        fromPanel.setLayout(new BoxLayout(fromPanel, BoxLayout.PAGE_AXIS));
        JTextField fromTextField = new JTextField(15);
        fromPanel.add(fromTextField);
        JComboBox fromCB = new JComboBox();
        fromCB.addActionListener(new ComboBoxMeasureActionLsitener(fromTextField));
        fromCB.setVisible(false);
        fromPanel.add(fromCB);

        JPanel viaPanel = new JPanel();
        viaPanel.setLayout(new BoxLayout(viaPanel, BoxLayout.PAGE_AXIS));
        JButton viaAdd = new JButton("Add via");
        viaAdd.addActionListener(new ViaAddActionListener(this, viaPanel));
        viaPanel.add(viaAdd);


        JPanel toPanel = new JPanel();
        toPanel.setLayout(new BoxLayout(toPanel, BoxLayout.PAGE_AXIS));
        JTextField toTextField = new JTextField(15);
        toPanel.add(toTextField);
        JComboBox toCB = new JComboBox();
        toCB.addActionListener(new ComboBoxMeasureActionLsitener(toTextField));
        toCB.setVisible(false);
        toPanel.add(toCB);

        Vector<JTextField> passLocations = new Vector<JTextField>();
        passLocations.add(fromTextField);
        passLocations.add(toTextField);

        JPanel submitAndAnswer = new JPanel();
        submitAndAnswer.setLayout(new BoxLayout(submitAndAnswer, BoxLayout.PAGE_AXIS));
        JTextArea measureResponse = new JTextArea("Measure response");
        measureResponse.setEditable(false);
        JButton measureSubmit = new JButton("Submit");
        measureSubmit.setName("measureSubmit");
        measureSubmit.addActionListener(new measureActionListener(fromPanel, viaPanel, toPanel, measureResponse));
        submitAndAnswer.add(measureSubmit);
        submitAndAnswer.add(measureResponse);

        measurePanel.add(lb);
        measurePanel.add(fromPanel);
        measurePanel.add(viaPanel);
        measurePanel.add(toPanel);
        measurePanel.add(submitAndAnswer);

        JScrollPane scrollpane = new JScrollPane(measurePanel);

        lay.add(scrollpane, new Integer(2));
        lay.add(zoomPanel, new Integer(2));
        lay.add(ww, new Integer(1));

        infoTag = new JPanel();
        lay.add(infoTag, new Integer(3));
        infoTag.setVisible(false);

        ww.setBounds(0, 0, this.getWidth(), this.getHeight() - 50);
        //Buttons and panels positions
        surfacePanel.setBounds(10, 133, 160, 30);





        ruler.setBounds(175, 20, 50, 50);
        zoomPanel.setBounds(10, 480, 200, 100);
        scrollpane.setBounds(10, 170, 200, 300);
        cp.add(lay);
        setVisible(true);
    }

    public void addGraphicButtons(JLayeredPane lay) {
        lay.add(createStandardButton("images/flatearth.jpg",
                "globe",
                new Rectangle(10, 20, 50, 50)),
                new Integer(2));

        //Enable-Disable Political Boudaries button setup        
        lay.add(createStandardButton("images/boundaries.jpg",
                "boundaries",
                new Rectangle(65, 20, 50, 50)),
                new Integer(2));



        //Earthquakes button setup
        lay.add(createStandardButton("images/earthquake.jpg",
                "earthquake",
                new Rectangle(120, 20, 50, 50)),
                new Integer(2));

        //Weather button setup
        lay.add(createStandardButton("images/weather.jpg",
                "weather",
                new Rectangle(10, 75, 50, 50)),
                new Integer(2));

        //Screenshot button setup
        lay.add(createStandardButton("images/screenshot.png",
                "ss",
                new Rectangle(65, 75, 50, 50)),
                new Integer(2));

        //Open file button Setup
        lay.add(createStandardButton("images/openFile.jpg",
                "open",
                new Rectangle(120, 75, 50, 50)),
                new Integer(2));
    }

    public JButton createStandardButton(String imageAddr, String name, Rectangle r) {
        JButton bt = new JButton(new ImageIcon(imageAddr));
        bt.setBorder(null);
        bt.setName(name);
        bt.addMouseListener(new StandardMouseListener(this, ww));
        bt.setBounds(r);
        return bt;
    }

    /*
     * Show a description panel near button
     * @title - description title
     * @description - description content
     */
    public void tagContent(String title, String description, Point p) {
        infoTag.removeAll();
        infoTag.updateUI();
        JTextField jtf = new JTextField(title, 10);
        jtf.setBorder(null);
        JTextArea jta = new JTextArea(description);
        infoTag.add(jtf);
        infoTag.add(jta);
        //ralative to window
        infoTag.setBounds(p.x - this.getLocation().x, p.y - this.getLocation().y, 180, 50);
    }


    /*
     * Disable a specific layer by name
     * @layerName - layer to be disabled
     */
    public void disableLayer(String layerName) {
        LayerList layers = ww.getModel().getLayers();
        for (Layer layer : layers) {
            if (layer.toString().compareTo(layerName) == 0) {
                layer.setEnabled(false);
            }
        }
    }

    /**
     * Default control buttons
     */
    public void setDefaultControls() {
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        ww.addSelectListener(new ViewControlsSelectListener(ww, viewControlsLayer));
        LayerList layers = ww.getModel().getLayers();
        layers.add(0, viewControlsLayer);
    }



    void standardDialogBox(String title, String content) {
        JDialog standardDialogBox = new JDialog(this, title, true);
        standardDialogBox.setLocation((screenSize.width - standardDialogBox.getWidth()) / 2, (screenSize.height - standardDialogBox.getHeight()) / 2);
        standardDialogBox.setMinimumSize(new Dimension(400, 300));
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        JLabel titleC = new JLabel("Exception!");
        JTextArea contentC = new JTextArea(content);
        container.add(titleC);
        container.add(contentC);
        standardDialogBox.add(container);

        standardDialogBox.pack();
        standardDialogBox.show();


    }
    /*
     * Creates the menu bar with about and exit items
     */

    final public JMenuBar setMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("  File  ");
        fileMenu.setMnemonic(KeyEvent.VK_N);
        menuBar.add(fileMenu);

        JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_N);
        exitMenuItem.setName("exit");
        exitMenuItem.addActionListener(new MenuActionListener(this));
        fileMenu.add(exitMenuItem);

        JMenu helpMenu = new JMenu("  Help  ");
        helpMenu.setMnemonic(KeyEvent.VK_N);
        menuBar.add(helpMenu);

        JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_N);
        aboutMenuItem.setName("about");
        aboutMenuItem.addActionListener(new MenuActionListener(this));
        helpMenu.add(aboutMenuItem);
        return menuBar;
    }
    /*
     * return true if the globe is flat
     */

    public boolean isFlatGlobe() {
        return ww.getModel().getGlobe() instanceof FlatGlobe;
    }



    /*
     * @jtf -  string for search
     * returns a list with all point found
     */

    public java.util.List<PointOfInterest> textFieldPoints(String jtf) {
        String toFind = jtf.trim();
        java.util.List<PointOfInterest> poi = new ArrayList<PointOfInterest>();
        String[] keyWordsBrute = toFind.split("[,]");
        Vector<String> keyWords = new Vector<String>();
        gz = new YahooGazetteer();
        for (int i = 0; i < keyWordsBrute.length; i++) {
            if (keyWordsBrute[i].trim().compareTo("") != 0) {
                keyWords.add(keyWordsBrute[i].trim());
            }
        }

        if (keyWords.isEmpty()) {
            return poi;
        }
        if (keyWords.size() == 1) {
            poi = gz.findPlaces(keyWords.elementAt(0));
            return poi;
        }
        if (keyWords.size() == 2) {
            Matcher matcher = (Pattern.compile("[0-9]")).matcher(keyWordsBrute[1]); //Street Address may have numbers in first field so use 2nd
            if (matcher.find()) {
                keyWordsBrute[0] = keyWordsBrute[0].trim();
                keyWordsBrute[1] = keyWordsBrute[1].trim();
                poi.add(parseCoordinates(keyWordsBrute));
            } else {
                poi = gz.findPlaces(keyWordsBrute[0].trim() + "+" + keyWordsBrute[1].trim());
            }
            return poi;
        }
        if (keyWords.size() > 2) {
            String buff = new String();
            for (int i = 0; i < keyWords.size(); i++) {
                buff = buff + keyWords.elementAt(i) + "+";
            }
            buff = buff.substring(0, buff.length() - 1);
            poi = gz.findPlaces(buff);
            return poi;
        }
        return poi;


    }


    /*
     * if in the text field are passed coordinates
     * @coords[] has two elements longitude and latitude
     */
    public PointOfInterest parseCoordinates(String coords[]) {
        if (isDecimalDegrees(coords)) {
            Double d1 = Double.parseDouble(coords[0].trim());
            Double d2 = Double.parseDouble(coords[1].trim());
            return new BasicPointOfInterest(LatLon.fromDegrees(d1, d2));
        } else //may be in DMS
        {
            Angle aLat = Angle.fromDMS(coords[0].trim());
            Angle aLon = Angle.fromDMS(coords[1].trim());
            return new BasicPointOfInterest(LatLon.fromDegrees(aLat.getDegrees(), aLon.getDegrees()));
        }
    }
    /*
     * if they are numbers
     * @coords[] - vector to be verified, it has two elements
     */

    private boolean isDecimalDegrees(String[] coords) {
        try {
            Double.parseDouble(coords[0].trim());
            Double.parseDouble(coords[1].trim());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    /*
     * load image when the open file button is pressed
     */

    public void loadImage() {
        JFileChooser fc = new JFileChooser(new File("."));
        int returnVal = fc.showOpenDialog(ww);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File path = fc.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(path);
                ImageIcon img = new ImageIcon(image);

                JFrame wf = new JFrame("Image");
                wf.setMinimumSize(new Dimension(1000, 600));
                wf.setLocation((screenSize.width - wf.getWidth()) / 2, (screenSize.height - wf.getHeight()) / 2);
                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
                if (image.getHeight() > 640 || image.getWidth() > 3360) {
                    img = new ImageIcon(img.getImage().getScaledInstance(640, 360, java.awt.Image.SCALE_SMOOTH));
                }

                JLabel imgLabel = new JLabel(img);
                container.add(imgLabel);
                JPanel buttons = new JPanel();
                JButton print = new JButton("Print");
                print.addActionListener(new PrintImageActionListener(this, image));
                buttons.add(print);
                container.add(buttons);


                wf.add(container);
                wf.setVisible(true);
            } catch (IOException ex) {
                standardDialogBox("Output exception", "Error trying writing file");
            }
        }
    }
    /*
     * take screenshot when screenshot button is pressed
     */

    public void takeScreenshot() {
        JFrame wf = new JFrame("Image");
        wf.setMinimumSize(new Dimension(1000, 600));
        wf.setLocation((screenSize.width - wf.getWidth()) / 2, (screenSize.height - wf.getHeight()) / 2);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        try {
            Rectangle screenRect = new Rectangle(this.getLocationOnScreen().x, this.getLocationOnScreen().y, this.getSize().width, this.getSize().height);
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            ImageIcon imgIcon = new ImageIcon(capture);
            imgIcon = new ImageIcon(imgIcon.getImage().getScaledInstance(640, 360, java.awt.Image.SCALE_SMOOTH));
            JLabel img = new JLabel(imgIcon);
            container.add(img);
            JPanel buttons = new JPanel();
            JButton save = new JButton("Save");
            JButton print = new JButton("Print");
            save.addActionListener(new SaveImageActionListener(this, capture));
            print.addActionListener(new PrintImageActionListener(this, capture));
            buttons.add(save);
            buttons.add(print);
            container.add(buttons);
        } catch (Exception e) {
            standardDialogBox("Output exception", "Error trying writing file");
        }
        wf.add(container);
        wf.setVisible(true);
    }




    /*
     * Creates the JFrame with weather
     */

    public void weatherFrame() {

        JFrame wf = new JFrame("Weather");
        wf.setMinimumSize(new Dimension(600, 500));
        wf.setLocation((screenSize.width - wf.getWidth()) / 2, (screenSize.height - wf.getHeight()) / 2);


        JPanel container = new JPanel();
        /*Location*/
        JPanel locPanel = new JPanel();
        JLabel locLabel = new JLabel("Location:");
        JTextField locTextField = new JTextField(20);
        locPanel.add(locLabel);
        locPanel.add(locTextField);

        /*Spinner*/
        SpinnerModel model = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner days = new JSpinner(model);
        JLabel daysLabel = new JLabel("Number of days:");
        JPanel dayPanel = new JPanel();
        dayPanel.add(daysLabel);
        dayPanel.add(days);

        /* Weather status*/
        JLabel weatherIcon = new JLabel();
        weatherIcon.setBounds(0, 0, 64, 64);
        weatherIcon.setVisible(false);

        /*Date*/
        JLabel date = new JLabel();
        JLabel rain = new JLabel();
        JLabel temperature = new JLabel();
        JLabel weatherStatus = new JLabel();
        JLabel wind = new JLabel();
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
        JPanel pageNum = new JPanel();

        JPanel info = new JPanel();
        info.setLayout(new GridLayout(4, 2));
        //Put pieces together
        info.add(date);
        info.add(weatherStatus);
        info.add(weatherIcon);

        info.add(rain);
        info.add(buttons);
        info.add(wind);
        info.add(pageNum);
        info.add(temperature);

        /*Submit button*/
        JButton submit = new JButton("Get weather");
        submit.addActionListener(new GetWeather(this, locTextField, days, weatherIcon, date, weatherStatus, temperature, rain, wind, buttons, pageNum));

        locPanel.add(submit);
        container.add(locPanel);
        container.add(dayPanel);
        container.add(info);

        wf.add(container);
        wf.setVisible(true);


    }
    /*
     * Characteristics for an earthquake
     */

    class EarthquakeContentElement {

        String title, summary, position, magnitude, details, image;

        public EarthquakeContentElement(String title,
                String image,
                String summary,
                Double lat,
                Double lon,
                Double elevation,
                Double magnitude,
                String Details) {
            this.title = title;
            this.image = image;
            this.summary = summary;
            this.position = "<html><p>" + lat.toString() + "</p><p>" + lon.toString() + "</p><p>" + elevation.toString() + "</p></html>";
            this.magnitude = magnitude.toString();
            this.details = Details;
        }
    }
    /*
     * class for mathematic earthquake attributes
     */

    class EarthquakeAttributes {

        Double longitude, latitude, elevation, magnitude;

        public EarthquakeAttributes(Double longitude, Double latitude, Double elevation, Double magnitude) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.elevation = elevation;
            this.magnitude = magnitude;
        }
    }
    /*
     * Listener for creating the earthquakes report in JFrame
     */

    class EarthqakeActionListener implements ActionListener {

        JTable table;
        JTextField jtf;
        Vector<EarthquakeContentElement> elem;
        JButton prev, next;

        public EarthqakeActionListener(JTable table, JTextField jtf, Vector<EarthquakeContentElement> elem, JButton prev, JButton next) {
            this.table = table;
            this.jtf = jtf;
            this.elem = elem;
            this.prev = prev;
            this.next = next;
        }

        public void actionPerformed(ActionEvent e) {
            JButton bt = (JButton) e.getSource();
            Integer i = Integer.parseInt(jtf.getText());
            i--;
            if (bt.getName().compareTo("next") == 0) {
                i++;
                table.setValueAt(elem.elementAt(i).title, 0, 0);
                table.setValueAt(elem.elementAt(i).image, 0, 1);
                table.setValueAt(elem.elementAt(i).summary, 0, 2);
                table.setValueAt(elem.elementAt(i).position, 0, 3);
                table.setValueAt(elem.elementAt(i).magnitude, 0, 4);
                table.setValueAt(elem.elementAt(i).details, 0, 5);
                jtf.setText("" + (i.intValue() + 1));
                if (i == elem.size() - 1) {
                    bt.setEnabled(false);
                } else {
                    prev.setEnabled(true);
                }
            }
            if (bt.getName().compareTo("prev") == 0) {
                i--;
                table.setValueAt(elem.elementAt(i).title, 0, 0);
                table.setValueAt(elem.elementAt(i).image, 0, 1);
                table.setValueAt(elem.elementAt(i).summary, 0, 2);
                table.setValueAt(elem.elementAt(i).position, 0, 3);
                table.setValueAt(elem.elementAt(i).magnitude, 0, 4);
                table.setValueAt(elem.elementAt(i).details, 0, 5);
                jtf.setText("" + (i.intValue() + 1));
                if (i == 0) {
                    bt.setEnabled(false);
                } else {
                    next.setEnabled(true);
                }
            }
        }
    }
    /*
     * Creates the earthquakes JFrame
     */

    public void eartquakeFrame() {
        try {
            JFrame eqf = new JFrame("Earthquakes");
            eqf.setMinimumSize(new Dimension(1000, 400));
            eqf.setLocation((screenSize.width - eqf.getWidth()) / 2, (screenSize.height - eqf.getHeight()) / 2);

            URL link = new URL("http://earthquake.usgs.gov/earthquakes/catalogs/7day-M2.5.xml");
            URLConnection yc = link.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            Vector<EarthquakeContentElement> oneRow = new Vector<EarthquakeContentElement>();
            Vector<EarthquakeAttributes> eqattr = new Vector<EarthquakeAttributes>();
            String atomLine;
            //Parsing...
            while ((atomLine = in.readLine()) != null) {
                if (atomLine.indexOf("<entry>") == -1) {
                    continue;
                }
                int l1 = atomLine.indexOf("<title>") + 7;
                if (l1 == 6) {
                    continue;
                }
                int l2 = atomLine.indexOf("</title>", l1);
                String Title = atomLine.substring(l1, l2);
                //Titles.add(Title);
                l1 = atomLine.indexOf("href=", l2) + 6;
                l2 = atomLine.indexOf(">", l1) - 2;

                String details = "<html><a href=\"" + atomLine.substring(l1, l2) + "\">" + "Click here" + "</a></html>";

                l1 = atomLine.indexOf("<img src=", l2);
                l2 = atomLine.indexOf(">", l1) + 1;
                String imageAddr = "<html>" + atomLine.substring(l1, l2) + "</html>";

                l1 = atomLine.indexOf("<p>", l2 - 1);
                l2 = atomLine.indexOf("</p>", l1) + 4;
                String smry = "<html>" + atomLine.substring(l1, l2);

                l1 = atomLine.indexOf("<p>", l2 - 1);
                l2 = atomLine.indexOf("</p>", l1) + 4;
                smry += atomLine.substring(l1, l2) + "</html>";

                l1 = atomLine.indexOf("<georss:point>", l1) + ("<georss:point>").length();
                l2 = atomLine.indexOf("</georss:point>", l1);
                String Latlon = atomLine.substring(l1, l2);

                Double lat = Double.parseDouble(Latlon.substring(0, Latlon.indexOf(" ")));
                Double lon = Double.parseDouble(Latlon.substring(Latlon.indexOf(" ")));

                l1 = atomLine.indexOf("<georss:elev>", l2) + ("<georss:elev>").length();
                l2 = atomLine.indexOf("</georss:elev>", l1);
                Double elev = Double.parseDouble(atomLine.substring(l1, l2));
                Double magnitude = Double.parseDouble(Title.substring(Title.indexOf(" "), Title.indexOf(",")));
                oneRow.add(new EarthquakeContentElement(Title, imageAddr, smry, lat, lon, elev, magnitude, details));
                eqattr.add(new EarthquakeAttributes(lon, lat, elev, magnitude));
            }
            in.close();

            JPanel mainPanel = new JPanel();
            mainPanel.add(new JLabel("Cutremure"));
            mainPanel.add(new JLabel(" "));
            mainPanel.add(new JLabel(" "));


            String[] columnsName = {"Title", "Image", "Summary", "Position", "Magnitude", "More details"};
            Object[][] data = new Object[1][6];

            JTable table = new JTable(data, columnsName);
            table.setRowHeight(0, 160);

            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

            eqf.setLayout(new GridLayout(1, 2));
            JPanel buttonsPanel = new JPanel();
            JButton previous = new JButton("Previous");
            previous.setName("prev");
            previous.setEnabled(false);
            JButton next = new JButton("Next");
            next.setName("next");

            if (oneRow.size() < 2) {
                next.setEnabled(false);
            }
            JTextField pgNum = new JTextField(4);
            pgNum.setEditable(false);
            pgNum.setText("1");

            JTextField maxPgNum = new JTextField(4);
            maxPgNum.setText("" + oneRow.size());

            next.addActionListener(new EarthqakeActionListener(table, pgNum, oneRow, previous, next));
            previous.addActionListener(new EarthqakeActionListener(table, pgNum, oneRow, previous, next));

            buttonsPanel.add(previous);
            buttonsPanel.add(next);
            buttonsPanel.add(pgNum);
            buttonsPanel.add(maxPgNum);

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
            mainPanel.add(table.getTableHeader());
            if (!oneRow.isEmpty()) {
                table.setValueAt(oneRow.elementAt(0).title, 0, 0);
                table.setValueAt(oneRow.elementAt(0).image, 0, 1);
                table.setValueAt(oneRow.elementAt(0).summary, 0, 2);
                table.setValueAt(oneRow.elementAt(0).position, 0, 3);
                table.setValueAt(oneRow.elementAt(0).magnitude, 0, 4);
                table.setValueAt(oneRow.elementAt(0).details, 0, 5);
            }
            JButton genHTML = new JButton("Generate HTML Report");
            genHTML.addActionListener(new GenHTMLActionListener(oneRow, eqattr));
            mainPanel.add(table);
            container.add(mainPanel);
            container.add(buttonsPanel);
            container.add(genHTML);
            eqf.add(container);
            eqf.setVisible(true);
        } catch (Exception e) {
            standardDialogBox("Input Output exception", "Something wrong with the link");


        }

    }
    /*
     * listener for generating HTML reports
     */

    class GenHTMLActionListener implements ActionListener {

        Vector<EarthquakeContentElement> tbContent;
        Vector<EarthquakeAttributes> eqattr;

        public GenHTMLActionListener(Vector<EarthquakeContentElement> tbContent, Vector<EarthquakeAttributes> eqattr) {
            this.tbContent = tbContent;
            this.eqattr = eqattr;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fc = new JFileChooser(new File("."));
                int returnVal = fc.showSaveDialog(ww);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter f = new FileWriter(fc.getSelectedFile());
                        BufferedWriter out = new BufferedWriter(f);
                        out.write("<html>\n");
                        out.write("<head>\n");
                        out.write("<title>");
                        out.write("Earthquakes report");
                        out.write("</title>\n");
                        out.write("<script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n");
                        out.write("<script type=\"text/javascript\">\n");
                        out.write("google.load('visualization', '1.1', {packages: ['controls']});\n");
                        out.write("</script>\n");
                        out.write("<script type=\"text/javascript\">");
                        out.write("function drawVisualization() {\n"
                                + "var data = google.visualization.arrayToDataTable([\n"
                                + "['Title', 'Image', 'Summary', 'Position', 'Magnitude', 'Details', 'Latitude', 'Longitude', 'Elevation', 'Magnitude', 'Location']\n");
                        if (tbContent.size() != 0) {
                            out.write(",\n");
                        }
                        for (int i = 0; i < tbContent.size(); i++) {

                            String location = tbContent.elementAt(i).title.substring(tbContent.elementAt(i).title.indexOf(",") + 2, tbContent.elementAt(i).title.length());
                            out.write("['" + tbContent.elementAt(i).title + "', '" + tbContent.elementAt(i).image + "', '" + tbContent.elementAt(i).summary
                                    + "', '" + tbContent.elementAt(i).position + "', " + tbContent.elementAt(i).magnitude + ", '" + tbContent.elementAt(i).details + "'," + eqattr.elementAt(i).latitude + ", "
                                    + eqattr.elementAt(i).longitude + ", " + eqattr.elementAt(i).elevation + ", " + eqattr.elementAt(i).magnitude + ", '" + location + "']");
                            if (i + 1 != tbContent.size()) {
                                out.write(",\n");
                            }

                        }
                        out.write("]);\n");

                        out.write("var latitude = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'latitude',\n"
                                + "'options': {\n"
                                + "  'filterColumnLabel': 'Latitude',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var longitude = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'longitude',\n"
                                + "'options': {\n"
                                + "  'filterColumnLabel': 'Longitude',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var elevation = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'elevation',\n"
                                + "'options': {\n"
                                + "  'filterColumnLabel': 'Elevation',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var magnitude = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'NumberRangeFilter',\n"
                                + "'containerId': 'magnitude',\n"
                                + "'options': {\n"
                                + " 'filterColumnLabel': 'Magnitude',\n"
                                + "'ui': {'labelStacking': 'vertical'}\n"
                                + "}\n"
                                + "});\n"
                                + "var stringFilter = new google.visualization.ControlWrapper({\n"
                                + "'controlType': 'StringFilter',\n"
                                + "'containerId': 'string_filter',\n"
                                + "'options': {\n"
                                + "'filterColumnLabel': 'Location'\n"
                                + "}\n"
                                + "});\n\n"
                                + "var table = new google.visualization.ChartWrapper({\n"
                                + "'chartType': 'Table',\n"
                                + "'containerId': 'chart3',\n"
                                + "'view': {'columns': [0, 1, 2, 3, 4, 5]},\n"
                                + "'options': {\n"
                                + "       allowHtml: true,\n"
                                + "       'dataTable' : data,\n"
                                + "'width': '1200px'\n"
                                + "}});\n\n");

                        out.write("myView = new google.visualization.DataView(data);"
                                + "new google.visualization.Dashboard(document.getElementById('dashboard')).bind([stringFilter, latitude, longitude, elevation, magnitude], [table]).draw(myView );\n"
                                + "}\n"
                                + "google.setOnLoadCallback(drawVisualization);\n"
                                + "</script>\n");

                        out.write("<style type=\"text/css\">\n"
                                + "body{\n"
                                + "	background-attachment:fixed;\n"
                                + "	color:#900;\n"
                                + "}\n"
                                + "</style>\n"
                                + "</head>\n"
                                + "<body background=\"weatherTexture.jpg\">\n"
                                + "<center>\n"
                                + "<div id=\"dashboard\">\n"
                                + "<div id=\"string_filter\"></div>\n"
                                + "<table width=\"200\" border=\"0\">\n"
                                + "  <tr>\n"
                                + "    <td><div id=\"latitude\"></div></td>\n"
                                + "    <td><div id=\"longitude\"></div></td>\n"
                                + "  </tr>\n"
                                + "  <tr>\n"
                                + "    <td><div id=\"elevation\"></div></td>\n"
                                + "    <td><div id=\"magnitude\"></div></td>\n"
                                + "  </tr>\n"
                                + "</table>\n"
                                + "<div id=\"control1\"></div>\n"
                                + "<div id=\"chart3\"></div>\n"
                                + "</div>\n"
                                + "</center>\n"
                                + "</body>\n"
                                + "</html>");
                        out.close();
                        f.close();
                    } catch (Exception exp) {
                        standardDialogBox("Output exception", "Error trying saving file");
                    }
                }
            } catch (Exception ex) {
                standardDialogBox("Output exception", "Error trying writing file");
            }
        }
    }
    /*
     * Listener for exiting the main frame
     */

    class MenuActionListener implements ActionListener {

        JFrame f;

        public MenuActionListener(JFrame f) {
            this.f = f;
        }

        public void actionPerformed(ActionEvent e) {
            if (((JMenuItem) e.getSource()).getName().compareTo("exit") == 0) {
                System.exit(-1);
            }
            if (((JMenuItem) e.getSource()).getName().compareTo("about") == 0) {
                aboutWindow();
            }
        }
    }
    /*
     * closing the "about" window
     */

    class ButtonActionListener implements ActionListener {

        JDialog f;

        public ButtonActionListener(JDialog f) {
            this.f = f;
        }

        public void actionPerformed(ActionEvent e) {
            if (((JButton) e.getSource()).getName().compareTo("exit") == 0) {
                f.dispose();
            }
        }
    }

    /*
     * About window
     * 
     */
    final public void aboutWindow() {
        //Initiate window
        JDialog fr = new JDialog(this, "Despre", true);
        fr.setLocation((screenSize.width - fr.getWidth()) / 2, (screenSize.height - fr.getHeight()) / 2);
        fr.setMinimumSize(new Dimension(300, 200));

        //Main Container
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        //Title
        JLabel title = new JLabel("About info:");
        //Content
        JTextArea content = new JTextArea("OOP Project.Made by Mihai Cosmin,\nstudent at Polytechnic university of Bucharest");
        content.setEditable(false);
        content.setBorder(null);
        //Closing button
        JButton closeButton = new JButton("Close");
        closeButton.setName("exit");
        closeButton.addActionListener(new ButtonActionListener(fr));
        //Put pieces together
        container.add(title);
        container.add(content);
        container.add(closeButton);
        fr.add(container);
        //Show window
        fr.setVisible(true);


    }

    /*
     * action listener for measuring the distance between several points on the globe
     */
    class measureActionListener implements ActionListener {

        JPanel Pfrom;
        JPanel panel;
        JPanel Pto;
        JTextArea jta;

        public measureActionListener(JPanel Pfrom, JPanel panel, JPanel Pto, JTextArea jta) {
            this.Pfrom = Pfrom;
            this.panel = panel;
            this.Pto = Pto;
            this.jta = jta;
        }

        public void actionPerformed(ActionEvent e) {
            JTextField from = (JTextField) Pfrom.getComponent(0);
            JTextField to = (JTextField) Pto.getComponent(0);
            JComboBox jcbf = ((JComboBox) Pfrom.getComponent(1));
            JComboBox jcbt = ((JComboBox) Pto.getComponent(1));

            jcbf.removeAllItems();
            jcbt.removeAllItems();
            jcbf.setVisible(false);
            jcbt.setVisible(false);

            java.util.List<PointOfInterest> combined = new ArrayList<PointOfInterest>();
            if (textFieldPoints(from.getText()).isEmpty()) {
                mkr.clear();
                positions.clear();
                standardDialogBox("Incorrect input", "Input is incorrect!");
                return;
            }
            ww.getView().goTo(new Position(textFieldPoints(from.getText()).get(0).getLatlon(), 0), 3000e3);
            combined.add((textFieldPoints(from.getText()).get(0)));
            for (int i = 0; i < panel.getComponentCount(); i++) {
                if (i % 3 == 1) {
                    if (!(textFieldPoints(((JTextField) panel.getComponent(i)).getText()).isEmpty())) {
                        JComboBox jcbm = (JComboBox) panel.getComponent(i + 2);
                        jcbm.setVisible(false);
                        if (textFieldPoints(((JTextField) panel.getComponent(i)).getText()).size() > 1) {
                            jcbm.removeAllItems();
                            java.util.List<PointOfInterest> each = textFieldPoints(((JTextField) panel.getComponent(i)).getText());
                            for (PointOfInterest poi : each) {
                                jcbm.addItem(poi);
                            }
                            jcbm.setVisible(true);

                        }
                        combined.add((textFieldPoints((((JTextField) panel.getComponent(i)).getText())).get(0)));
                    } else {
                        continue;
                    }
                }
            }
            if ((textFieldPoints(to.getText())).isEmpty()) {
                standardDialogBox("Incorrect input", "Input is incorrect!");
                return;
            }
            if (textFieldPoints(from.getText()).size() > 1) {
                java.util.List<PointOfInterest> each = textFieldPoints(from.getText());
                for (PointOfInterest poi : each) {
                    jcbf.addItem(poi);
                }
                jcbf.setVisible(true);

            }
            if (textFieldPoints(to.getText()).size() > 1) {
                java.util.List<PointOfInterest> each = textFieldPoints(to.getText());
                for (PointOfInterest poi : each) {
                    jcbt.addItem(poi);
                }
                jcbt.setVisible(true);
            }
            combined.add((textFieldPoints(to.getText()).get(0)));
            PointOfInterest poi1, poi2;
            double dist = 0;
            Iterator it = combined.iterator();
            poi2 = (PointOfInterest) it.next();
            for (int i = 1; i < combined.size(); i++) {
                poi1 = poi2;
                poi2 = (PointOfInterest) it.next();
                dist += LatLon.ellipsoidalDistance(poi1.getLatlon(), poi2.getLatlon(), 6378.1370, 6356.2569);
            }
            if (dist > 0.001) {
                jta.setText((new Double(dist)).toString() + " Km");
            } else {
                jta.setText("Aceeasi locatie");
            }

            it = combined.iterator();
            positions.clear();
            mkr.clear();
            MarkerAttributes attr = new BasicMarkerAttributes(Material.RED, BasicMarkerShape.SPHERE, 1d, 10, 5);
            for (int i = 0; i < combined.size(); i++) {
                Position poz = new Position(((PointOfInterest) it.next()).getLatlon(), 0);
                positions.add(poz);
                Marker mk = new BasicMarker(poz, attr);
                mk.setPosition(poz);
                mkr.add(mk);
            }
            Mlayer.setMarkers(mkr);
            segm.setPositions(positions);
            ww.redraw();
        }
    }

    public static void main(String args[]) throws IOException {
        new WWJ();
    }
}