
                echo "<div class=\"form-group\">";
                echo "<fieldset>";
                echo "<legend>Gruppe</legend>";

                echo form_dropdown(
                    'team',
                    $teams,
                    NULL,
                    array(
                        'id' => 'team',
                        'class' => 'custom-select form-control',
                        'disabled' => 'disabled')
                );

                echo "</fieldset>";
                echo "</div>";
                echo "<div class=\"form-group\">";
                echo "<legend>Datum</legend>";

                echo form_input(array(
                    'id'=>'datepicker',
                    'name'=>'datepicker',
                    'class'=>'form-control',
                    'value'=> isset($meeting['meetingdate']) ? $meeting['meetingdate'] : ""));

                echo "</div>";
                echo "<div class=\"form-group\">";
                echo "<legend>Beschreibung / Kommentar</legend>";

                echo form_input(array(
                    'id'=>'description',
                    'name'=>'description',
                    'class'=>'form-control',
                    'value'=> isset($meeting['description']) ? $meeting['description'] : ""));

                echo "</div>";
                echo "<div class=\"form-group\">";
                echo "<fieldset>";
                echo "<legend>Teilnehmer</legend>";

                foreach($participants as $participantname => $participant) {

                    echo "<div class=\"custom-control custom-checkbox\">";

                    $data = array(
                        'id' => $participant['idparticipant'],
                        'value' => $participant['idparticipant'],
                        'name' => 'participantids[]',
                        'class' => "custom-control-input",
                        'checked' => isset($participant['value']) && $participant['value'] == 1 ? 'checked' : ''
                    );
                    if(!isset($participant['isparticipant'])) {
                        $data['disabled'] = 'disabled';
                    }

                    echo form_checkbox($data);
                    echo form_label($participantname, $participant['idparticipant'], "class=\"custom-control-label\"");       

                    echo "</div>";
                }

                echo "</fieldset>";
                echo "</div>";
                ?>
                
            