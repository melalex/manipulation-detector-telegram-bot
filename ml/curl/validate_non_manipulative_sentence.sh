curl -X POST http://localhost:3000/ner \
  -H "Content-Type: application/json" \
  -d '{"text": "Фаріон виклала листа від хлопця з окупованого Криму, який висловив їй підтримку.\nВона не приховала його персональні дані і відмовилась це робити після зауважень.\nРосіяни вже затримали хлопця та виклали з ним відео."}'
