package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.presentation.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.forvia.droidcon.common.composables.HtmlText
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.model.CarPropertiesDataModel

@Composable
fun CarDetailsCard(
    carPropertiesDataModel: CarPropertiesDataModel,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 45.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            carPropertiesDataModel.value?.let {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Area: ",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                        )
                        Text(
                            text = carPropertiesDataModel.area.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White
                            ),
                        )
                    }
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Value: ",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            ),
                        )
                        Text(
                            text = carPropertiesDataModel.value,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.White,
                            ),
                        )
                    }

                    HorizontalDivider()
                }
            }

            item {
                Column {
                    Text(
                        text = "Description: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                    )
                    Spacer(Modifier.height(16.dp))
                    HtmlText(
                        html = carPropertiesDataModel.propertyDescription,
                        textColor = Color.White
                    )
                }
            }
        }
    }
}
