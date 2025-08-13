export class CattleTransfersTableModel {
    id: number;
    transfer_type: string;
    origin_farm: string;
    destination_farm: string;
    transfer_date: string;
    observations: string;
    created_by: string;

    constructor(
        id: number,
        transfer_type: string,
        origin_farm: string,
        destination_farm: string,
        transfer_date: string,
        observations: string,
        created_by: string
    ) {
        this.id = id;
        this.transfer_type = transfer_type;
        this.origin_farm = origin_farm;
        this.destination_farm = destination_farm;
        this.transfer_date = transfer_date;
        this.observations = observations;
        this.created_by = created_by;
    }
}