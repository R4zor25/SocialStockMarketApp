package hu.bme.aut.android.socialstockmarketapp.ui.uicomponent

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.material.datepicker.*
import hu.bme.aut.android.socialstockmarketapp.ui.theme.MyBlue
import java.util.*

@Composable
fun DatePickerview(
    datePicked : String?,
    updatedDate : ( date : Long? ) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .background(MyBlue)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable{
                showDatePicker(activity, updatedDate)
            }
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            val (lable, iconView) = createRefs()

            Text(
                text= datePicked?:"Date Picker",
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lable) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconView.start)
                        width = Dimension.fillToConstraints
                    }
            )

            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = Color.Black
            )

        }

    }
}

private fun showDatePicker(
    activity : AppCompatActivity,
    updatedDate: (Long?) -> Unit)
{
    val constraintsBuilder = CalendarConstraints.Builder()
    val validators: ArrayList<CalendarConstraints.DateValidator> = ArrayList()

    constraintsBuilder.setValidator(CompositeDateValidator.allOf(validators))
    val calendar = Calendar.getInstance()
    val upTo = calendar.timeInMillis
    calendar.add(Calendar.YEAR, -1)
    val startFrom = calendar.timeInMillis
    validators.add(DateValidatorPointForward.from(startFrom))
    validators.add(DateValidatorPointBackward.before(upTo))
    val picker = MaterialDatePicker
        .Builder
        .datePicker()
        .setCalendarConstraints(constraintsBuilder.build())
        .build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        updatedDate(it)
    }
}