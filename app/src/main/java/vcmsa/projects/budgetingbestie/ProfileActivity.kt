package vcmsa.projects.budgetingbestie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnEditProfile: Button
    private lateinit var btnAchievements: Button
    private lateinit var btnNotifications: Button
    private lateinit var btnLogout: Button
    private lateinit var imgProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Bind views
        btnEditProfile = findViewById(R.id.btnEdit)
        btnAchievements = findViewById(R.id.btnAchievements)
        btnNotifications = findViewById(R.id.btnNotifications)
        btnLogout = findViewById(R.id.btnLogout)
        imgProfile = findViewById(R.id.imgProfile)

        // ADD ACTIVITY SCREENS FOR BUTTONS
        btnEditProfile.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, UserActivity::class.java))
        }

        // Toast only for other buttons
        btnAchievements.setOnClickListener {
            Toast.makeText(this, "Achievements clicked", Toast.LENGTH_SHORT).show()
        }

        btnNotifications.setOnClickListener {
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
        }

        imgProfile.setOnClickListener {
            Toast.makeText(this, "Profile image clicked", Toast.LENGTH_SHORT).show()
        }
    }
}