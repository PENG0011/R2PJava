package begin;

//SystemPermission 类使用简单的条件逻辑管理访问软件系统的许可状态
//SystemPermission 中的 state 实例变量会在 request，claimed，denied 和 granted 等状态之间转换
public class SystemPermission {
    public final static String REQUESTED = "REQUESTED";
    public final static String CLAIMED = "CLAIMED";
    public final static String GRANTED = "GRANTED";
    public final static String DENIED = "DENIED";
    public final static String UNIX_REQUESTED = "UNIX_REQUESTED";
    public final static String UNIX_CLAIMED = "UNIX_CLAIMED";

    private SystemProfile profile;
    private SystemUser requestor;
    private SystemAdmin admin;
    private boolean isGranted;
    private boolean isUnixPermissionGranted;
    private String state;

    public SystemPermission(SystemUser requestor, SystemProfile profile) {
        this.requestor = requestor;
        this.profile = profile;
        state = REQUESTED;
        isGranted = false;
        notifyAdminOfPermissionRequest();
    }

    public void claimedBy(SystemAdmin admin) {
        if (!state.equals(REQUESTED) && !state.equals(UNIX_REQUESTED))
            return;
        willBeHandledBy(admin);
        if (state.equals(REQUESTED))
            state = CLAIMED;
        else if (state.equals(UNIX_REQUESTED))
            state = UNIX_CLAIMED;
    }

    public void deniedBy(SystemAdmin admin) {
        if (!state.equals(CLAIMED) && !state.equals(UNIX_CLAIMED))
            return;
        if (!this.admin.equals(admin))
            return;
        isGranted = false;
        isUnixPermissionGranted = false;
        state = DENIED;
        notifyUserOfPermissionRequestResult();
    }

    public void grantedBy(SystemAdmin admin) {
        if (!state.equals(CLAIMED) && !state.equals(UNIX_CLAIMED))
            return;
        if (!this.admin.equals(admin))
            return;

        if (profile.isUnixPermissionRequired() && state.equals(UNIX_CLAIMED))
            isUnixPermissionGranted = true;
        else if (profile.isUnixPermissionRequired() && !isUnixPermissionGranted()) {
            state = UNIX_REQUESTED;
            notifyUnixAdminsOfPermissionRequest();
            return;
        }
        state = GRANTED;
        isGranted = true;
        notifyUserOfPermissionRequestResult();
    }

    private void notifyAdminOfPermissionRequest() {
        System.out.println("Permission Requested.");
    }

    private void willBeHandledBy(SystemAdmin admin) {
        this.admin = admin;
        System.out.println("Permission Claimed.");
    }

    private void notifyUnixAdminsOfPermissionRequest() {
        System.out.println("UNIX Permission Requested.");
    }

    private void notifyUserOfPermissionRequestResult() {
        if (isGranted)
            System.out.println("Permission Granted.");
        else
            System.out.println("Permission NOT Granted.");
    }

    public String state() {
        return state;
    }

    public boolean isGranted() {
        return isGranted;
    }

    private boolean isUnixPermissionGranted() {
        return isUnixPermissionGranted;
    }
}