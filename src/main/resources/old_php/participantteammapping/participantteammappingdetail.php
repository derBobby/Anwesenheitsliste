    <main class="container pt-5">
        <div class="card mb-3">
            <div class="card-header"><?php echo $title ?></div>
            <div class="card-block p-3">
            
<?php

if(isset($critical)) {
    
    echo "<p class = \"text-warning\">$critical</p>";
        
} else {
    
    echo form_open($function);

    echo validation_errors("<p class = \"text-warning\">", "</p>"); //TODO new row?

    if(isset($error) && !empty($error)) {
        echo "<p class = \"text-warning\">$error</p>";
    }

    echo form_hidden('idparticipant', isset($idparticipant) ? $idparticipant : "");

    echo "<div class=\"form-group\">";
    echo "<fieldset>";
    echo "<legend>Teilnehmer</legend>";
    
    $extra = array(
        'id' => 'participant',
        'class' => 'custom-select form-control',
        'disabled' => 'disabled');
    
    echo form_dropdown(
        'participant',
        $participants,
        NULL,
        $extra);
    
    echo "</fieldset>";
    echo "</div>";
    
    echo "<div class=\"form-group\">";
    echo "<fieldset>";
    echo "<legend>Gruppen</legend>";

    foreach($teams as $teamname => $team) {

        echo "<div class=\"custom-control custom-checkbox\">";

        echo form_checkbox(array(
        'id' => $team['idteam'],
        'value' => $team['idteam'],
        'name' => 'teamids[]',
        'class' => "custom-control-input",
        'checked'=> isset($team['value']) && $team['value'] == 1 ? "checked" : ""));
        echo form_label($teamname, $team['idteam'], "class=\"custom-control-label\"");       

        echo "</div>";
    }
    
    echo "</fieldset>";
    echo "</div>";
    
?>
                <div class="btn-group">
<?php

    echo form_submit(array(
        'id'=>'submit',
        'value'=>'Speichern',
        'class'=>'btn btn-primary')); 
?>
                    
                    <a href="<?php echo base_url('/ParticipantList'); ?>" class="btn btn-secondary">
                        <i class="fa fa-times"></i> 
                    </a>
                </div>

<?php
    echo form_close(); 

}

?> 

            </div>
        </div>
    </main>