package end;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class SystemPermissionTest_UNIX_End {
    private SystemPermission permission;
    private SystemUser user;
    private SystemProfile profile;
    private SystemAdmin admin;

    @Before
    public void setUp() throws Exception {
        user = new SystemUser();
        profile = new SystemProfile(true); // 需要 UNIX 权限
        admin = new SystemAdmin();
        permission = new SystemPermission(user, profile);
    }

    @Test
    public void testClaimedBy() {
        // 直接 Claim 操作
        // 结果：CLAIMED 状态， isGranted = false
        permission.claimedBy(admin);
        assertEquals("claimed", PermissionState.CLAIMED, permission.getState());
        assertEquals("claimed", false, permission.isGranted());

        // 先 Claim 再进行 Grant 操作，
        // 结果：UNIX_REQUESTED 状态， isGranted = false
        permission.grantedBy(admin);
        assertEquals("granted", PermissionState.UNIX_REQUESTED, permission.getState());
        assertEquals("granted", false, permission.isGranted());

        // 再进行 Claim 操作
        // 结果：UNIX_CLAIMED 状态， isGranted = false
        permission.claimedBy(admin);
        assertEquals("granted", PermissionState.UNIX_CLAIMED, permission.getState());
        assertEquals("granted", false, permission.isGranted());
    }

    @Test
    public void testDeniedBy() {
        // 未 Claim 的状态进行 Denied 操作，
        // 结果：REQUESTED 状态， isGranted = false
        permission.grantedBy(admin);
        assertEquals("requested", PermissionState.REQUESTED, permission.getState());
        assertEquals("not granted", false, permission.isGranted());

        // 先 Claim 再进行 Grant 操作，进入 UNIX_REQUESTED 状态
        // 继续 Claim 再进行 Denied 操作
        // 结果：DENIED 状态， isGranted = false
        permission.claimedBy(admin);
        permission.grantedBy(admin);
        permission.claimedBy(admin);
        permission.deniedBy(admin);
        assertEquals("granted", PermissionState.DENIED, permission.getState());
        assertEquals("granted", false, permission.isGranted());
    }

    @Test
    public void testGrantedBy() {
        // 未 Claim 的状态进行 Grant 操作，
        // 结果：REQUESTED 状态， isGranted = false
        permission.grantedBy(admin);
        assertEquals("requested", PermissionState.REQUESTED, permission.getState());
        assertEquals("not granted", false, permission.isGranted());

        // 先 Claim 再进行 Grant 操作，进入 UNIX_REQUESTED 状态
        // 继续 Claim 再进行 Grant 操作
        // 结果：GRANTED 状态， isGranted = true
        permission.claimedBy(admin);
        permission.grantedBy(admin);
        permission.claimedBy(admin);
        permission.grantedBy(admin);
        assertEquals("granted", PermissionState.GRANTED, permission.getState());
        assertEquals("granted", true, permission.isGranted());
    }
}
