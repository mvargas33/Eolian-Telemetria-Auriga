export default function () {
  return {
    mainData: [56, 3.4, 1.024, 89, 4.048, 3.876, 34.5, 27.9], // 8 [velocidad, potencia, mppt_in, soc, max_volt, min_volt, max_temp, min_temp]
    kelly_der: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13], // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
    kelly_izq: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13], // 14 [ia, ib, ic, va, vb, vc, rpm, err_code, pwm, emr, motor_temp, kelly_temp, throttle, reverse]
    bms: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14], // 15 [SOC, current, inst_volt, open_volt, abs_current, max_volt, min_volt, max_temp, max_temp_id, min_temp, min_temp_id, avg_temp, internal_temp, max_volt_id, min_volt_id]
    bms_temp: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], // 60
    bms_volt: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], // 30
    mppt1: [0, 0, 0, 0, 0, 0, 0, 0], // 8 [uin, iin, uout, blvr, ovt, noc, undv, temp]
    mppt2: [0, 0, 0, 0, 0, 0, 0, 0], // 8
    mppt3: [0, 0, 0, 0, 0, 0, 0, 0], // 8
    mppt4: [0, 0, 0, 0, 0, 0, 0, 0], // 8
    potencia_mppt: [0, 0, 0, 0] // 4 [pot_mppt1, pot_mppt2, pot_mppt3, pot_mppt4]
  }
}
