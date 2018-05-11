    <main class="container pt-5">
        <div class="card mb-3">
            <?php echo form_open($function); ?>
            
            <div class="card-header">
                <?php echo $title ?>
            </div>
            
            <div class="card-block p-3">
    
                <?php  
                echo validation_errors("<p class = \"text-warning\">", "</p>"); //TODO new row?

                if(isset($error) && !empty($error)) {
                    echo "<p class = \"text-warning\">$error</p>";
                }

                echo form_hidden('idmeeting', isset($meeting['idmeeting']) ? $meeting['idmeeting'] : "");
                echo form_hidden('idteam', $idteam);

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
                
            </div>
            
            <div class="card-footer">

                <div class="btn-group">
                    <?php
                    echo form_submit(array(
                        'id'=>'submit',
                        'value'=>'Speichern',
                        'class'=>'btn btn-primary')
                    ); 
                    ?>    
                    <a href="<?php echo base_url("/MeetingList/index/$idteam"); ?>" class="btn btn-secondary">
                        <i class="fa fa-times"></i> 
                    </a>
                </div>
                 
            </div>
        
        <?php echo form_close(); ?> 
        </div>
    </main>