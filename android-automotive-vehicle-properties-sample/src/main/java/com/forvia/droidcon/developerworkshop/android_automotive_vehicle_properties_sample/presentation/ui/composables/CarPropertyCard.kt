package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.actions.MainAction


@Composable
fun CarPropertyCard(
    modifier: Modifier = Modifier,
    onAction: (MainAction) -> Unit,
    carPropertiesDataModel: CarPropertiesDataModel,
    isSelected: Boolean,
) {

    val cardColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) Color.White else Color.Black

    Card(
        modifier = modifier
            .padding(horizontal = 45.dp, vertical = 8.dp)
            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                onAction(
                    MainAction.OnCarPropertySelected(carPropertiesDataModel)
                )
            },
        colors = CardDefaults.cardColors().copy(
            containerColor = cardColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight()
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = textColor
                        ),
                        text = carPropertiesDataModel.propertyName,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = textColor
                        ),
                        text = "ID: ${carPropertiesDataModel.propertyIdentifier}",
                    )
                }
                if (isSelected) {
                    Icon(
                        modifier = Modifier.rotate(90f),
                        imageVector = Icons.Default.KeyboardArrowUp,
                        tint = textColor,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}
