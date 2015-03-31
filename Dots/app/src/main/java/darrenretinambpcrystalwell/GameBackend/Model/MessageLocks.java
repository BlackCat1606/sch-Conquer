package darrenretinambpcrystalwell.GameBackend.Model;

/**
 * A simple object that contains four separate locks to be used in DotSocketHelper.
 *
 * Need to lock to prevent server from writing to the client at the same time in both threads
 * Need for separate locks as writing and reading from sockets can occur simultaneously
 *
 * Created by JiaHao on 31/3/15.
 */
public class MessageLocks {

    private final Object[] locks;

    public MessageLocks() {
        final int NO_OF_LOCKS = 4;

        this.locks = new Object[NO_OF_LOCKS];

        for (int i = 0; i < NO_OF_LOCKS; i++) {

            this.locks[i] = new Object();

        }
    }

    public Object getLock(int i) {
        return this.locks[i];
    }

}
