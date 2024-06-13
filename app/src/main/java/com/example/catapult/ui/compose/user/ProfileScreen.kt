import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catapult.datastore.UserData

@Composable
fun ProfileScreen(user: UserData) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "First Name: ${user.firstName}")
        Text(text = "Last Name: ${user.lastName}")
        Text(text = "Email: ${user.email}")
        Text(text = "Nickname: ${user.nickname}")
    }
}