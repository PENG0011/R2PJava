package begin;

public class SystemProfile {
    private boolean isUnixPermissionRequired;

    public SystemProfile(boolean isUnixPermissionRequired) {
        this.isUnixPermissionRequired = isUnixPermissionRequired;
    }

    public boolean isUnixPermissionRequired() {
        return isUnixPermissionRequired;
    }
}
