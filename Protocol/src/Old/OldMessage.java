public class OldMessage {/*
    private char header;
    private ArrayList<Component> components;    // Values and Sizes available
    private int firstComponentValueIndex;       // Índice del primer valor a usar en el primer componente
    private int lastComponentValueIndex;        // Índice del último valor a usar en el último componente
    private String typeOfCompression;           // Get size type
    private byte[] concatenationReceived;

    OldMessage(char h, String typeCompression, int firstComponentIndex){
        this.firstComponentValueIndex = firstComponentIndex;
        this.header = h;
        this.typeOfCompression = typeCompression;
        this.components = new ArrayList<>();
    }

    OldMessage(char header, ArrayList<Component> components, String compression){
        this.header = header;
        this.components = components;
        this.typeOfCompression = compression;
    }

    OldMessage(byte[] rawBytes, ArrayList<Component> allComponents){ // All Components ONLY to SELECT the ones that can be decrypted
        if(!checkCRC(rawBytes)){
            System.out.println("Cadena mal recibida, CRC no coinciden"); return;
        }
        this.concatenationReceived = rawBytes;  // Save raw bytes received
        this.header = (char) rawBytes[0];       // Save header
        setComponentsFromHeader(allComponents); // Select the components the message carries it's value
    }

    public void addComponent(Component c){
        this.components.add(c);
    }

    public void setFirstComponentValueIndex(int index){
        this.firstComponentValueIndex = index;
    }

    public int getFirstComponentValueIndex(){return firstComponentValueIndex;}
    public int getLastComponentValueIndex(){return lastComponentValueIndex;}

    public void setLastComponentValueIndex(int index){
        this.lastComponentValueIndex = index;
    }

    public byte[] encode(){
        return generateConcatenatedArrayWithCRC(header, joinArrays(extractValuesAndSizes()));
    }

    public ArrayList<int[][]> extractValuesAndSizes(){
        ArrayList<int[][]> list = new ArrayList<>();
        for (Component component : components) {
            list.add(component.getValuesAndSizes(typeOfCompression));
        }
        return list;
    }

    public void setComponentsFromHeader(ArrayList<Component> allComponents){
        switch (header){
            case 'a' :  this.components = allComponents;
            case 'b' :  for(int i = 0; i < 7; i++){
                this.components.add(allComponents.get(i));  // Kelly # 1 | Kelly # 2 | MPPT1 | MPPT2 | MPPT3 | MPPT4 | BMS_Core
            }
            case 'c' :  this.components.add(allComponents.get(7));      // BMS_Volt
                this.components.add(allComponents.get(8));      // BMS_Temp
            default  :  System.out.println("Header recibido no coincide con ningún header registrado");
        }
    }

    public void decode(){
        int[][] valuesAndSizes = joinArrays(extractValuesAndSizes());
        for(Component c : components){
            // Pasar valores de componentes a arrays para poder reccorrer linealmente y no asignar hardoceado.
        }
    }


    // arrays[0][:] : valores int
    // arrays[1][:] : largos de valores
    private int[][] joinArrays(ArrayList<int[][]> arrays){
        int n = 0;
        for (int[][] dataArray: arrays) {
            n += dataArray[0].length;
        }
        int[][] joinedData = new int[2][n];
        int i = 0;
        for (int[][] dataArray: arrays) {
            for (int j = 0; j < dataArray[0].length; j++){
                joinedData[0][i] = dataArray[0][j];
                joinedData[1][i] = dataArray[1][j];
                i++;
            }
        }
        return joinedData;
    }

    private byte[] generateConcatenatedArrayWithCRC(char header, int[][] valuesAndSizes){
        byte[] bytesOfData = (new ByteArray(valuesAndSizes[0], valuesAndSizes[1])).getByteArray(); // Encode
        byte[] concatenation = new byte[1 + bytesOfData.length + 2];
        concatenation[0] = (byte) header;
        System.arraycopy(bytesOfData, 0, concatenation, 1, concatenation.length - 2 - 1);
        return appendCRC(concatenation);
    }

    // MODBUS RTU CRC
    private static int getCRC(byte[] buf, int len){
        int crc = 0xFFFF;

        for (int pos = 0; pos < len; pos++) {
            crc ^= (int)buf[pos] & 0xFF;      // XOR byte into least sig. byte of crc

            for (int i = 8; i != 0; i--) {    // Loop over each bit
                if ((crc & 0x0001) != 0) {    // If the LSB is set
                    crc >>= 1;                // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                }
                else                          // Else LSB is not set
                    crc >>= 1;                // Just shift right
            }
        }
        // Note, this number (crc) has low and high bytes swapped, so use it accordingly (or swap bytes)
        return ((crc >> 8) & 0xFF) | ((crc << 8) & 0xFF00);
    }

    static byte[] appendCRC(byte[] data){
        int crc = getCRC(data, data.length);
        data[data.length - 2] = (byte) ((crc >> 8) & 0xFF);
        data[data.length - 1] = (byte) (crc & 0xFF);
        return data;
    }


    static boolean checkCRC(byte[] cadena){
        int crc = ((cadena[cadena.length - 2] & 0xFF) << 8) | (cadena[cadena.length - 1] & 0xFF);
        byte[] cadenaSinCRC = new byte[cadena.length];
        if (cadena.length - 2 >= 0) System.arraycopy(cadena, 0, cadenaSinCRC, 0, cadena.length - 2);
        cadenaSinCRC[cadena.length - 1] = 0;    // Último
        cadenaSinCRC[cadena.length - 2] = 0;    // Penúltimo
        //System.out.print("Cadena sin CRC:  ");printByteArray(cadenaSinCRC);
        int cadenaCRC = getCRC(cadenaSinCRC, cadenaSinCRC.length);
        if(!(cadenaCRC==crc)) {
            System.out.format("Lectura dañada: CRC no coinciden, es %d cuando debiera ser %d \n", cadenaCRC, crc);
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Header: ");sb.append(this.header);sb.append(" ");
        sb.append("Number of Components: ");sb.append(this.components.size());sb.append(" ");
        sb.append("First Component index: ");sb.append(firstComponentValueIndex);sb.append(" ");
        sb.append("Last Component index: ");sb.append(lastComponentValueIndex);sb.append(" ");
        sb.append("Components: ");
        for (Component c : this.components
        ) {
            sb.append(c.getNombre());sb.append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }

    public void removeLastComponent() {
        this.components.remove(this.components.size() - 1);
    }

    public ArrayList<Component> getComponents(){
        return components;
    }*/
}
