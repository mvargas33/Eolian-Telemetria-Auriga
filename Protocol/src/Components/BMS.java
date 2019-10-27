package Components;

import LocalSystems.LocalMasterAdmin;
import Protocol.Sending.SenderAdmin;

public class BMS extends Component{


    public BMS(LocalMasterAdmin myLocalMasterAdmin, int[] valores, int[] bitsSignificativos, String ID) {
        super(myLocalMasterAdmin, valores, bitsSignificativos, ID);
    }

    public BMS(SenderAdmin senderAdmin, int[] valores, int[] bitsSignificativos, String ID) {
        super(senderAdmin, valores, bitsSignificativos, ID);
    }

}
