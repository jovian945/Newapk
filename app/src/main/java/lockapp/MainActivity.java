package lockapp;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminComponent;
    private EditText pinInput;
    private String correctPIN = "1234"; // Ganti PIN di sini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // **Cek PIN dulu sebelum menampilkan UI**
        checkPIN();

        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);

        Button btnLock = findViewById(R.id.btnLock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (devicePolicyManager.isAdminActive(adminComponent)) {
                    devicePolicyManager.lockNow();
                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Aplikasi ini memerlukan izin untuk mengunci perangkat.");
                    startActivity(intent);
                }
            }
        });
    }

    private void checkPIN() {
        pinInput = new EditText(this);
        pinInput.setHint("Masukkan PIN");

        setContentView(pinInput);

        pinInput.setOnEditorActionListener((v, actionId, event) -> {
            String enteredPIN = pinInput.getText().toString();
            if (enteredPIN.equals(correctPIN)) {
                return false; // PIN benar, lanjutkan
            } else {
                Toast.makeText(this, "PIN salah!", Toast.LENGTH_SHORT).show();
                finish(); // Tutup aplikasi jika PIN salah
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Mencegah keluar dengan tombol back
    }
}