<?php

function get_command_mapping($filename) {
    function parse_objdump_line($line) {
        # Input is like this:
        # 6d941: 8b 75 1c                mov    0x1c(%rbp),%esi
        # Output: array of two items:
        # 8b 75 1c
        # mov    0x1c(%rbp),%esi
        if (!str_contains($line, ':')) {
            return ['', ''];
        }
        $line = explode(':', $line)[1];
        $hex = substr($line, 0, 23);
        $asm = substr($line, 23);
        return [trim($hex), trim($asm)];
    }

    $res = array();

    $output = shell_exec('objdump --disassemble ' . $filename);
    $lines = explode("\n", $output);

    for ($i = 0; $i < count($lines); $i++) {
        [$hex, $asm] = parse_objdump_line($lines[$i]);
        if ($hex == '') {
            continue;
        }
        [$next_hex, $next_asm] = $i == count($lines) - 1 ? ['', ''] : parse_objdump_line($lines[$i+1]);
        if ($next_asm == '') {
            $hex = trim($hex . ' ' . $next_hex);
        }
        if (in_array($hex, $res)) {
            if ($res[$hex] != $asm) {
                echo "Same bytecode parsed into different commands. \nBytecode: $hex\nOld value: " . $res[$hex] . "\nNew value: $asm\n";
                die();
            }
        }
        $res[$hex] = $asm;
    }

    return $res;
}

foreach (get_command_mapping($argv[1]) as $bytecode => $asm) {
    echo "$bytecode => $asm\n";
}

?>