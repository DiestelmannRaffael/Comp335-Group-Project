import datacontainers.dynamiccontainers.DynamicServer;

import java.util.Comparator;

public class ServerSorter implements Comparator<DynamicServer>{

    @Override
    public int compare(DynamicServer o1, DynamicServer o2) {
        return o1.getServerTypeValue() - o2.getServerTypeValue();
    }
}
